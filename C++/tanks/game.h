#pragma once
#include <algorithm>
#include "prog.h"
#include <ctime>
#include "gui/base_game_window.h"

class AI;


class Game : public gui::BaseGameWindow {
 public:
  vector<Tank*> vectTanks;
  vector<Wall*> vectWalls;
  vector<Shot*> vectShots;
  vector<Collizianable*> vectCollizianables;
  
  Game();

  void CreateLevel(int level);
  void AddObj(Collizianable* obj);
  void RemoveObj(Collizianable* obj);
  void Render(gdi::Graphics& canvas) override;
  void ProcessInput(const bool keys[256]);
  void Update(float dt, RealizeCollizion& collizions);
  void EnemyInput(float dt);
private:
	 Tank* player;
};


class AI : public Tank {
public:
	float last_decision_time = 0;
	action decision = Tank::right;

	void Update(float dt, Game* game) {
		if (last_decision_time > 2000) {
			// new decision
			last_decision_time = 0;
		}
		last_decision_time += dt;

		this->Control(decision, game);
	}
private:
};
