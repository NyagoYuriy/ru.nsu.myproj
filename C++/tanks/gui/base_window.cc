#include "base_window.h"

#include <CommCtrl.h>
#include <ShellScalingAPI.h>

#include <map>

#pragma comment(lib, "Comctl32")
#pragma comment(lib, "Shcore")

namespace gui {

#define GUI_BASE_CLASS_NAME L"~base~window~class~"

GUIInitializer* GUIInitializer::gui = nullptr;

struct GUIInitializer::Internal {
  using WindowsStorage = std::map<HWND, BaseWindow*>;
  WindowsStorage registered_windows;

  std::wstring empty_string;
};

namespace {

HINSTANCE GetAppInstance() {
  return GUIInitializer::gui->instance;
}

BaseWindow* FindRegisteredBaseWindow(HWND handle) {
  auto fnd = GUIInitializer::gui->internals->registered_windows.find(handle);
  if (fnd == GUIInitializer::gui->internals->registered_windows.end())
    return nullptr;
  return fnd->second;
}

void RegisterBaseWindow(HWND handle, BaseWindow* window) {
  GUIInitializer::gui->internals->registered_windows[handle] = window;
}

size_t UnregisterBaseWindow(BaseWindow* window) {
  GUIInitializer::gui->internals->registered_windows.erase(window->handle());
  return GUIInitializer::gui->internals->registered_windows.size();
}
}  // namespace

GUIInitializer::GUIInitializer(HINSTANCE app_instance) {
  // DCHECK(!gui);
  gui = this;
  internals.reset(new GUIInitializer::Internal);
  instance = app_instance;

  INITCOMMONCONTROLSEX controls;
  controls.dwSize = sizeof(INITCOMMONCONTROLSEX);
  controls.dwICC = ICC_STANDARD_CLASSES;
  ::InitCommonControlsEx(&controls);
}

GUIInitializer::~GUIInitializer() {}

//-----------------------------------------------------------------------------

BaseWindow::BaseWindow() {}

BaseWindow::~BaseWindow() {
  Destroy();
}

bool BaseWindow::Create(DWORD style_ex,
                        DWORD style,
                        int x,
                        int y,
                        int cx,
                        int cy,
                        HWND parent) {
  WNDCLASSEXW wce = {};
  wce.cbSize = sizeof(WNDCLASSEX);
  wce.style = CS_DBLCLKS;
  wce.lpfnWndProc = &BaseWindow::StaticInternalWndProc;
  wce.hInstance = GUIInitializer::gui->instance;

  // TODO(boocmp): base::ScopedGDIObject
  wce.hbrBackground = static_cast<HBRUSH>(::GetStockObject(WHITE_BRUSH));
  wce.hCursor = ::LoadCursor(nullptr, IDC_ARROW);

  wchar_t class_name[256] = GUI_BASE_CLASS_NAME;
  wce.lpszClassName = class_name;
  if (!PreRegister(&wce)) {
    return false;
  }

  WNDCLASSEXW wce_temp = {};
  if (!::GetClassInfoExW(GetAppInstance(), wce.lpszClassName, &wce_temp)) {
    // not registered.
    if (!::RegisterClassExW(&wce)) {
      return false;
    }
  }

  CreateParams params;
  params.class_name = wce.lpszClassName;
  params.style = style | WS_CLIPCHILDREN | WS_CLIPSIBLINGS;
  params.style_ex = style_ex;
  params.window_name = L"";
  params.x = x;
  params.y = y;
  params.cx = cx;
  params.cy = cy;

  if (!PreCreate(&params))
    return false;

  RECT rc = {x, y, x + params.cx, y + params.cy};
  ::AdjustWindowRectEx(&rc, params.style, false, params.style_ex);
  params.x = rc.left;
  params.y = rc.top;
  params.cx = rc.right - rc.left;
  params.cy = rc.bottom - rc.top;

  HWND handle =
      ::CreateWindowExW(params.style_ex, params.class_name, params.window_name,
                       params.style, params.x, params.y, params.cx, params.cy,
                       parent, nullptr, GetAppInstance(), this);
  if (handle) {
    BaseWindow* base_window = FindRegisteredBaseWindow(handle);
    if (!base_window) {
      // Attached window.
      this->parent_ = parent;
      RegisterBaseWindow(handle, this);
      if (!Attach(handle) || !WndProc(WM_CREATE, 0, 0)) {
        UnregisterBaseWindow(this);
        Destroy();
      }
    }
  }

  return handle != nullptr;
}

bool BaseWindow::Create(HWND parent) {
  return Create(0, WS_OVERLAPPEDWINDOW | WS_VISIBLE, CW_USEDEFAULT,
                CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, parent);
}

bool BaseWindow::Destroy() {
  if (::IsWindow(handle_)) {
    auto ret = !!::DestroyWindow(handle_);
    handle_ = nullptr;
    return ret;
  }
  return true;
}

BaseWindow::operator HWND() const {
  return handle();
}

HWND BaseWindow::handle() const {
  return handle_;
}

HWND BaseWindow::parent() const {
  return parent_;
}

int BaseWindow::anchors() const {
  return anchors_;
}

void BaseWindow::SetAnchors(int anchors) {
  anchors_ = anchors;
}

bool BaseWindow::Attach(HWND window) {
  if (subclass_props_.old_proc == nullptr) {
    if (!::IsWindow(window))
      return false;  // can attach only window.
    handle_ = window;
    subclass_props_.old_proc = reinterpret_cast<WNDPROC>(::SetWindowLongPtr(
        window, GWLP_WNDPROC,
        reinterpret_cast<LONG_PTR>(&BaseWindow::StaticInternalWndProc)));
    return true;
  }
  return false;
}

bool BaseWindow::Detach() {
  if (subclass_props_.old_proc != nullptr) {
    ::SetWindowLongPtr(handle(), GWLP_WNDPROC,
                       reinterpret_cast<LONG_PTR>(subclass_props_.old_proc));
    subclass_props_.old_proc = nullptr;
  }
  return true;
}

RECT BaseWindow::GetControlRect(HWND wnd) const {
  HWND parent_wnd = parent();
  if (!wnd) {
    wnd = handle();
  } else {
    parent_wnd = handle();
  }

  RECT rc;
  GetWindowRect(wnd, &rc);

  POINT pt;
  pt.x = rc.left;
  pt.y = rc.top;
  ScreenToClient(parent_wnd, &pt);
  rc.left = pt.x;
  rc.top = pt.y;

  pt.x = rc.right;
  pt.y = rc.bottom;
  ScreenToClient(parent_wnd, &pt);
  rc.right = pt.x;
  rc.bottom = pt.y;

  return rc;
}

bool BaseWindow::PreRegister(WNDCLASSEXW* wnd_class_ex) {
  return true;
}

bool BaseWindow::PreCreate(CreateParams* create_params) {
  return true;
}

LRESULT BaseWindow::WndProc(UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_CREATE: {
      if (!OnCreate(reinterpret_cast<LPCREATESTRUCT>(lParam)))
        return FALSE;
      SendMessage(handle(), WM_ANCHOR, 0, 0);
      return TRUE;
    }
    case WM_MOUSEMOVE: {
      TRACKMOUSEEVENT tme = {sizeof(TRACKMOUSEEVENT), TME_LEAVE, handle(), 0};
      _TrackMouseEvent(&tme);
      break;
    }
    case WM_DESTROY:
      if (subclass_props_.old_proc) {
        Detach();
      }
      if (UnregisterBaseWindow(this) == 0) {
        // main window closed, unregistering class and posting quit message
        ::UnregisterClassW(GUI_BASE_CLASS_NAME, GetAppInstance());
        ::PostQuitMessage(0);
      }
      handle_ = nullptr;
      break;
    case WM_PAINT: {
      RECT rc;
      GetClientRect(handle(), &rc);
      PAINTSTRUCT ps;
      BeginPaint(handle(), &ps);
      {
        gdi::Graphics ctx(ps.hdc);
        OnClientPaint(ctx, gdi::RectF(1.0f * rc.left, 1.0f * rc.top,
                                      1.0f * (rc.right - rc.left),
                                      1.0f * (rc.bottom - rc.top)));
      }
      EndPaint(handle(), &ps);
      return 1;
    }
    case WM_WINDOWPOSCHANGING: {
      LPWINDOWPOS pos = reinterpret_cast<LPWINDOWPOS>(lParam);
      if (pos->flags & SWP_NOSIZE)
        break;

      RECT rc;
      GetWindowRect(handle(), &rc);

      int w_delta = pos->cx - rc.right + rc.left;
      int h_delta = pos->cy - rc.bottom + rc.top;
      if (!w_delta && !h_delta)
        break;

      for (HWND h = ::GetTopWindow(handle()); h;
           h = ::GetNextWindow(h, GW_HWNDNEXT)) {
        ::SendMessage(h, WM_ANCHOR, 0, MAKELPARAM(w_delta, h_delta));
      }
      break;
    }
    case WM_CHILDRESIZED: {
      int w_delta = GET_X_LPARAM(lParam);
      int h_delta = GET_Y_LPARAM(lParam);

      RECT rc;
      GetClientRect(handle(), &rc);
      rc.right += w_delta;
      rc.bottom += h_delta;
      ::SetWindowPos(handle(), 0, 0, 0, rc.right, rc.bottom,
                     SWP_NOMOVE | SWP_NOZORDER);
      break;
    }
    case WM_ANCHOR: {
      if (anchors() == POS_NONE)
        return 0;

      RECT rc = GetControlRect();
      int width = rc.right - rc.left;
      int height = rc.bottom - rc.top;
      int w_delta = GET_X_LPARAM(lParam);
      int h_delta = GET_Y_LPARAM(lParam);

      if (anchors() & POS_RIGHT) {
        rc.right += w_delta;
        if (!(anchors() & POS_LEFT)) {
          rc.left = rc.right - width;
        }
      }

      if (anchors() & POS_BOTTOM) {
        rc.bottom += h_delta;
        if (!(anchors() & POS_TOP)) {
          rc.top = rc.bottom - height;
        }
      }

      ::MoveWindow(handle(), rc.left, rc.top, rc.right - rc.left,
                   rc.bottom - rc.top, TRUE);
      return 0;
    }
  }
  if (subclass_props_.old_proc) {
    return ::CallWindowProc(subclass_props_.old_proc, handle(), msg, wParam,
                            lParam);
  }
  return ::DefWindowProc(handle_, msg, wParam, lParam);
}

BOOL BaseWindow::OnCreate(LPCREATESTRUCT params) {
  return TRUE;
}

void BaseWindow::OnClientPaint(gdi::Graphics& ctx,
                                  const gdi::RectF& client_rect) {
}

void BaseWindow::OnDPIChanged(WORD dpi, float scale, const RECT& window_rect) {}

// static
LRESULT CALLBACK BaseWindow::StaticInternalWndProc(HWND hwnd,
                                                   UINT msg,
                                                   WPARAM wParam,
                                                   LPARAM lParam) {
  if (auto* window = FindRegisteredBaseWindow(hwnd)) {
    return window->WndProc(msg, wParam, lParam);
  }

  if (msg == WM_CREATE) {
    CREATESTRUCT* cs = reinterpret_cast<CREATESTRUCT*>(lParam);
    BaseWindow* base_window = reinterpret_cast<BaseWindow*>(cs->lpCreateParams);
    if (base_window) {
      base_window->handle_ = hwnd;
      base_window->parent_ = cs->hwndParent;

      RegisterBaseWindow(hwnd, base_window);
      LRESULT create_result = base_window->WndProc(msg, wParam, lParam);
      if (!create_result) {
        UnregisterBaseWindow(base_window);
      }
      return create_result;
    }
  }
  return ::DefWindowProc(hwnd, msg, wParam, lParam);
}

}  // namespace gui
