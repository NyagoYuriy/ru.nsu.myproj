#include "game.h"

Game::Game() : gui::BaseGameWindow(100, 100, 500, 500), player(new Tank(300, 300)) {}



void Game::AddObj(Collizianable* obj) {
	this->vectCollizianables.push_back(obj);
	if (obj->Name() == "tank")
		this->vectTanks.push_back(dynamic_cast<Tank*>(obj));
	if (obj->Name() == "wall")
		this->vectWalls.push_back(dynamic_cast<Wall*>(obj));
	if (obj->Name() == "shot")
		this->vectShots.push_back(dynamic_cast<Shot*>(obj));
}

void Game::RemoveObj(Collizianable* obj) {
	vectCollizianables.erase(std::find(vectCollizianables.begin(), vectCollizianables.end(), obj));
	if (obj->Name() == "tank")
		vectTanks.erase(std::find(vectTanks.begin(), vectTanks.end(), obj));
	if (obj->Name() == "wall")
		vectWalls.erase(std::find(vectWalls.begin(), vectWalls.end(), obj));
	if (obj->Name() == "shot")
		vectShots.erase(std::find(vectShots.begin(), vectShots.end(), obj));
}

void Game::CreateLevel(int level) {
  switch (level) {
    case 1:
      int cntWalls = 0;
      int x = 10, y = 10;
	  int health = 1000;
	  AddObj(player);
	  Tank* tank = new Tank(31, 31);
	  AddObj(tank);
	  
	  while (cntWalls != 76) {
		Wall* wall = new Wall(x, y, health);
	    AddObj(wall);
		cntWalls++;
        if (cntWalls < 20)
          y += vectWalls.back()->sizeY * 2;
        else if (cntWalls < 39)
          x += vectWalls.back()->sizeX * 2;
        else if (cntWalls < 58)
          y -= vectWalls.back()->sizeY * 2;
        else
          x -= vectWalls.back()->sizeX * 2;
      }
	  Wall* wall = new Wall(100, 100);
	  AddObj(wall);
	  break;
  }
}

void Game::Render(gdi::Graphics& canvas) {
  canvas.Clear(gdi::Color::Black);

 // player.Draw(canvas);

  for (Tank* tank : vectTanks)
    tank->Draw(canvas);
  for (Wall* wall : vectWalls)
    wall->Draw(canvas);
  for (Shot* shot : vectShots)
    shot->Draw(canvas);
}

void Game::ProcessInput(const bool keys[256]) {
	if (!player)
		return;
	if (keys['W'] || keys[VK_UP]) {
		player->Control(Tank::up, this);
	} else 	if (keys['S'] || keys[VK_DOWN]) {
		player->Control(Tank::down, this);
	} else 	if (keys['A'] || keys[VK_LEFT]) {
		player->Control(Tank::left, this);
	} else 	if (keys['D'] || keys[VK_RIGHT]) {
		player->Control(Tank::right, this);
	}
	if (keys[VK_SPACE])
		player->Control(Tank::fire, this);
}

void Game::Update(float dt, RealizeCollizion& collizions) {
	pair <int, int> delta;
	Collizianable* obj1, *obj2;


	for (Tank* tank : vectTanks) {
		if (tank->health <= 0) {
			RemoveObj(tank);
			if (tank == player)
				player = nullptr;
			delete tank;
		}
		else {
			tank->coordinates.first += tank->speed.first;
			tank->coordinates.second += tank->speed.second;
		}
	}

	for (int i=0; i<vectCollizianables.size(); i++)
				for (int j = i+1; j<vectCollizianables.size(); j++) {
					obj1 = vectCollizianables[i];
					obj2 = vectCollizianables[j];
					delta = collizions.CheckCollizion(*obj1, *obj2);
			if ((delta.first == 0 && delta.second == 0) || obj1 == obj2)
				continue;
			else 
				collizions.findClashFunc(obj1->Name(), obj2->Name()) (*obj1, *obj2, delta);
		}

	for (Tank* tank : vectTanks) {
			tank->speed.first = 0;
			tank->speed.second = 0;
	}


	for (Wall* wall : vectWalls) {
		if (wall->health <= 0) {
			RemoveObj(wall);
			wall->~Wall();
		}
	}
	for (Shot* shot : vectShots) {
		if (shot->health <= 0){
			RemoveObj(shot);
			shot->~Shot();
		}
		else {
			shot->coordinates.first += shot->speed.first;
			shot->coordinates.second += shot->speed.second;
			
		}
	
	}
}
void Game::EnemyInput(float dt) {
	for (Tank* tank : vectTanks) {
		if (player != tank) {
			tank->AIControl(dt, this);
		}
	}
}
/*
void Game::AIControl(float dt) {
	for (auto * ai : enemies) {
		ai->Update(dt, this);
	}
}
*/