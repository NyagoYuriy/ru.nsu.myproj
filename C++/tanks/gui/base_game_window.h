#pragma once

#include "base_window.h"

namespace gui {

class BaseGameWindow : public BaseWindow {
 public:
  BaseGameWindow(int x, int y, int width, int height);

  int GetWidth() const {
    RECT rc;
    GetClientRect(handle(), &rc);
    return rc.right;
  }

  int GetHeight() const {
    RECT rc;
    GetClientRect(handle(), &rc);
    return rc.bottom;
  }

  bool IsActive() const;

  virtual void Render(gdi::Graphics& canvas) = 0;

 private:
  LRESULT WndProc(UINT msg, WPARAM wParam, LPARAM lParam);
  void OnClientPaint(gdi::Graphics& ctx,
                     const gdi::RectF& client_rect) override;

  bool is_active_ = false;
  std::unique_ptr<gdi::Bitmap> double_buffer_;
};

}  // namespace gui
