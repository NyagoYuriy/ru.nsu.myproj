#include "prog.h"

string Shot::Name() {
	return "shot";
}

Shot::~Shot() {

}


void Shot::Draw(gdi::Graphics& canvas) {
	canvas.FillRectangle(&gdi::SolidBrush(gdi::Color(255, 255, 255)),
		coordinates.first - 2, coordinates.second - 2, 6, 6);
}