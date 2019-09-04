#pragma once

#include <Windows.h>
#include <windowsx.h>
#include <gdiplus.h>

#include <memory>
#include <string>

namespace gdi = Gdiplus;

class GdiPlus {
 public:
  GdiPlus() {
    Gdiplus::GdiplusStartupInput input;
    input.SuppressBackgroundThread = TRUE;
    Gdiplus::GdiplusStartup(&token_, &input, &hooks_);
    hooks_.NotificationHook(&token_);
  }

  ~GdiPlus() {
    hooks_.NotificationUnhook(token_);
    Gdiplus::GdiplusShutdown(token_);
  }

 private:
  ULONG_PTR token_;
  Gdiplus::GdiplusStartupOutput hooks_;
};

namespace gui {

class GUIInitializer {
 public:
  struct Internal;

  explicit GUIInitializer(HINSTANCE app_instance);
  ~GUIInitializer();

  static GUIInitializer* gui;
  std::unique_ptr<Internal> internals;
  HINSTANCE instance = nullptr;
};

enum Anchors {
  POS_NONE = 0,
  POS_LEFT = 1,
  POS_RIGHT = 2,
  POS_TOP = 4,
  POS_BOTTOM = 8,
  POS_CENTER = (POS_LEFT | POS_RIGHT | POS_TOP | POS_BOTTOM),
  POS_ALL = POS_CENTER
};

constexpr UINT WM_ANCHOR = WM_USER + 100;
constexpr UINT WM_CHILDRESIZED = WM_USER + 101;

class BaseWindow {
 public:
  BaseWindow();
  virtual ~BaseWindow();

  bool Create(DWORD style_ex,
              DWORD style,
              int x,
              int y,
              int cx,
              int cy,
              HWND parent);

  bool Create(HWND parent);
  bool Destroy();

  operator HWND() const;
  HWND handle() const;
  HWND parent() const;
  int anchors() const;

  void SetAnchors(int anchors);

 protected:
  struct CreateParams {
    DWORD style = 0;
    DWORD style_ex = 0;

    int x = 0;
    int y = 0;
    int cx = 0;
    int cy = 0;

    LPCWSTR window_name = nullptr;
    LPCWSTR class_name = nullptr;
  };

  struct SubclassProperies {
    WNDPROC old_proc = nullptr;
    RECT client_rect = {};
  };

  bool Attach(HWND window);
  bool Detach();

  RECT GetControlRect(HWND wnd = nullptr) const;

  virtual bool PreRegister(WNDCLASSEXW* wnd_class_ex);
  virtual bool PreCreate(CreateParams* create_params);

  virtual LRESULT WndProc(UINT msg, WPARAM wParam, LPARAM lParam);

  virtual BOOL OnCreate(LPCREATESTRUCT params);
  virtual void OnClientPaint(gdi::Graphics& ctx, const gdi::RectF& client_rect);
  virtual void OnDPIChanged(WORD dpi, float scale, const RECT& window_rect);

  HWND handle_ = nullptr;
  HWND parent_ = nullptr;
  int anchors_ = POS_NONE;

  SubclassProperies subclass_props_;

 private:
  static LRESULT CALLBACK StaticInternalWndProc(HWND hwnd,
                                                UINT msg,
                                                WPARAM wParam,
                                                LPARAM lParam);
  WORD current_dpi_ =  USER_DEFAULT_SCREEN_DPI;
};

}  // namespace gui
