#include "base_game_window.h"
#include <iostream>

namespace gui {

BaseGameWindow::BaseGameWindow(int x, int y, int width, int height) {
  RECT rc = {0, 0, width, height};
  AdjustWindowRect(&rc, WS_BORDER | WS_SYSMENU | WS_VISIBLE, FALSE);
  Create(WS_EX_APPWINDOW, WS_BORDER | WS_SYSMENU | WS_VISIBLE, x, y, rc.right,
         rc.bottom, nullptr);
}

bool BaseGameWindow::IsActive() const {
  return is_active_;
}

LRESULT BaseGameWindow::WndProc(UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
    case WM_SIZE: {
      auto w = GET_X_LPARAM(lParam);
      auto h = GET_Y_LPARAM(lParam);
      gdi::Graphics ctx(handle());
      double_buffer_.reset(new gdi::Bitmap(w, h, &ctx));
    } break;
    case WM_ERASEBKGND:
      return FALSE;
    case WM_ACTIVATE: {
      is_active_ = wParam != WA_INACTIVE;
      break;
    }
  }

  return BaseWindow::WndProc(msg, wParam, lParam);
}

void BaseGameWindow::OnClientPaint(gdi::Graphics& ctx,
                                   const gdi::RectF& client_rect) {
  gdi::Graphics dc(double_buffer_.get());
  Render(dc);

  ctx.DrawImage(double_buffer_.get(), client_rect);
}

}  // namespace gui
