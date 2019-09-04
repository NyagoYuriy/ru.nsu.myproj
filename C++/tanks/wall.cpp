#include "prog.h"

Wall::Wall(int x, int y, int health) {
	this->coordinates = MakePair(x, y);
	this->health = health;
}

void Wall::Draw(gdi::Graphics& canvas) {
  int r = 255 * (health / 1000.0f);
  canvas.FillRectangle(&gdi::SolidBrush(gdi::Color(r, 50, 50)),
  coordinates.first - 10, coordinates.second - 10, 20, 20);
}


string Wall::Name() {
	return "wall";
}