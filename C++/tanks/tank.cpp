
#include "game.h"

Tank::Tank(int x, int y) {
	this->coordinates = MakePair(x, y);
	this->health = 3;	
}
Tank::~Tank() {

}
Shot* Tank::CreateShot(int health) {
	Shot* shot = new Shot;
	shot->health = health;
	shot->direction = this->direction;
	if (shot->direction == "up") {
		shot->coordinates.first = this->coordinates.first;
		shot->coordinates.second = this->coordinates.second - (this->sizeY + 12);
		shot->speed.second = -3;
	}
	if (shot->direction == "down") {
		shot->coordinates.first = this->coordinates.first;
		shot->coordinates.second = this->coordinates.second + (this->sizeY + 12);
		shot->speed.second = 3;
	}
	if (shot->direction == "left") {
		shot->coordinates.first = this->coordinates.first - (this->sizeX + 12);
		shot->coordinates.second = this->coordinates.second;
		shot->speed.first = -3;
	}
	if (shot->direction == "right") {
		shot->coordinates.first = this->coordinates.first + (this->sizeX + 12);
		shot->coordinates.second = this->coordinates.second;
		shot->speed.first = 3;
	}
	return shot;
}
void Tank::Control(enum action a, Game* game) {
	switch (a) { 
	case left: 
		speed.first = -2;
		direction = "left";
		break;
	case right: 
		speed.first = 2;
		direction = "right";
		break;
	case up: 
		speed.second = -2;
		direction = "up";
		break;
	case down: 
		speed.second = 2;
		direction = "down";
		break;
	case fire: 
		if (game->vectShots.empty()) {
			game->AddObj(this->CreateShot(1));
			cout << game->vectShots[0]->health;
		}
		break;
  }
}


string Tank::Name() {
	return "tank";
}
void Tank::PushOut(pair <int, int> delta) {
	if (this->speed.first > 0)
		this->coordinates.first -= delta.first + 1;
	if (this->speed.first < 0)
		this->coordinates.first += delta.first + 1;
	if (this->speed.second > 0)
		this->coordinates.second -= delta.second + 1;
	if (this->speed.second < 0)
		this->coordinates.second += delta.second + 1;
}

void Tank::Draw(gdi::Graphics& canvas) {
  canvas.FillRectangle(&gdi::SolidBrush(gdi::Color::Yellow),
                       coordinates.first - 10, coordinates.second - 10, 20, 20);
}

void Tank::AIControl(float dt, Game* game) {
	cout << dt;
	last_update += dt;
	if (last_update > 1.5 || last_decision == fire) {
		srand(time(0));
		int random = rand() % 5;
		if (random == 0)
			last_decision = up;
		if (random == 1)
			last_decision = right;
		if (random == 2)
			last_decision = down;
		if (random == 3)
			last_decision = left;
		if (random == 4)
			last_decision = fire;
		last_update = 0;
	}
	Control(last_decision, game);
	Control(fire, game);
}



	/*cout << dt;
	last_update += dt;
	if (last_update < 4)
		this->Control(right, game);
	else if (last_update < 8)
		this->Control(down, game);
	else if (last_update < 12)
		this->Control(left, game);
	else if (last_update < 16)
		this->Control(up, game);
	else
		last_update = 0;
	*/
	
	
	/*
	this->Control(tmp, game);
 	if (last_update > 2) {
		if (direction == "right")
			tmp = down;
			//this->Control(down, game);
		if (direction == "down")
			tmp = left;
			//this->Control(up, game);
		if (direction == "left")
			tmp = up;
			//this->Control(right, game);
		if (direction == "up")
			tmp = right;
		if (last_update == 0)
			tmp = fire;
			//this->Control(fire, game);
		last_update = 0;
	}
	*/
