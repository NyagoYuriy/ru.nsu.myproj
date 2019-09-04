#include <stdio.h>
#include <iostream>
#include "game.h"

struct DebugConsole {
  DebugConsole() {
    AllocConsole();
    freopen_s(&old_, "CONOUT$", "wt", stdout);
  }
  ~DebugConsole() {
    fflush(stdout);

    FreeConsole();
    fclose(old_);
  }

 private:
  FILE* old_ = nullptr;
};



#pragma comment(lib, "gdiplus")
#pragma comment(lib, "winmm")

int CALLBACK WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR lpCmdLine,
                     int nCmdShow) {
  DebugConsole debug_console_holder;
  GdiPlus graphics;
  gui::GUIInitializer gui(hInstance);

  Game game;
  game.CreateLevel(1);
  RealizeCollizion collizions;

  timeBeginPeriod(1);
  DWORD ticks = timeGetTime();
  MSG msg;
  for (;;) {
    float dt = (timeGetTime() - ticks) / 1000.0f;
    ticks = timeGetTime();

    while (PeekMessage(&msg, 0, 0, 0, PM_REMOVE)) {
      if (msg.message == WM_QUIT)
        return 0;
      TranslateMessage(&msg);
      DispatchMessage(&msg);
    }

    game.Update(dt, collizions);
    BYTE sys_keys[256];
    bool keys[256];
    GetKeyboardState(sys_keys);
	for (int i = 0; i < 256; ++i)
      keys[i] = (sys_keys[i] & 0x80) == 0x80;
	
    game.ProcessInput(keys);
	game.EnemyInput(dt);
	InvalidateRect(game, 0, FALSE);
  }

  timeEndPeriod(1);

  return static_cast<int>(msg.wParam);
}
