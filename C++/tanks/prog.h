#pragma once
#include <string>
#include <map>
#include <vector>
#include <iostream>

#include "gui\base_window.h"

using namespace std;
class Collizianable;
class Drawable;
class Game;
using ptrClashFunc = void(*)(Collizianable&, Collizianable&, pair <int, int> delta);
using clashMap = map <pair<string, string>, ptrClashFunc>;

class Drawable {

public:
	virtual void Draw(gdi::Graphics& canvas) = 0;
private:
};

class Collizianable {
public:
	pair<int, int> coordinates;
	const int sizeX = 10, sizeY = 10;
	pair<int, int> MakePair(int x, int y);
/*
	pair<int, int> CheckCollizion(Collizianable& obj1, Collizianable& obj2);
	bool DotInTheBox(pair <int, int> point, pair <int, int> upperLeft, pair <int, int> rightBottom);
	*/
	virtual ~Collizianable() {}
	virtual string Name() = 0;
private:
};

class Shot : public Collizianable,  public Drawable {
public:
	
	int health = 1;
	string direction = "up";
	pair <int, int> speed;
	Shot() {};
	void Draw(gdi::Graphics& canvas) override;
	~Shot();
	string Name();
private:
	
};


class Tank : public Collizianable, public Drawable {
public:
	int health = 3;
	string direction = "up";
	enum action {none, left, right, up, down, fire};
	pair <int, int> speed;
	

	Tank() {};
	Tank(int x, int y);
    void Control(enum action, Game* game);
	Shot* CreateShot(int health = 1);
	void PushOut(pair <int, int> delta);
	void Draw(gdi::Graphics& canvas) override;
	void AIControl(float dt, Game* game);
	~Tank();
	string Name();
private:
	float last_update = 0;
	Tank::action last_decision = right;
	 
	
	

};

class Wall : public Collizianable, public Drawable{
public:
	Wall() {};
	Wall(int x, int y, int health = 1);
	int health;
	void Draw(gdi::Graphics& canvas) override;
	string Name();
private:
	

};


class RealizeCollizion {
public:
	RealizeCollizion();
	
	pair<int, int> CheckCollizion(Collizianable& obj1, Collizianable& obj2);
	bool DotInTheBox(pair <int, int> point, pair <int, int> upperLeft, pair <int, int> rightBottom);
	pair<int, int> MakePair(int x, int y);



	static void TankShot(Collizianable& tank, Collizianable& shot, pair <int, int> delta);
	static void TankWall(Collizianable& tank, Collizianable& wall, pair <int, int> delta);
	static void ShotWall(Collizianable& shot, Collizianable& wall, pair <int, int> delta);
	static void TankTank(Collizianable& tank1, Collizianable& tank2, pair <int, int> delta);
	static void ShotShot(Collizianable& shot1, Collizianable& shot2, pair <int, int> delta);
	//vice verse
	static void ShotTank(Collizianable& shot, Collizianable& tank, pair <int, int> delta);
	static void WallTank(Collizianable& wall, Collizianable& tank, pair <int, int> delta);
	static void WallShot(Collizianable& wall, Collizianable& shot, pair <int, int> delta);

	static void UnknownClash(Collizianable& obj1, Collizianable& obj2, pair <int, int> delta);

	clashMap mapCollizion;
	clashMap* initClashMap();

	//map<string, string> mapType;
	//map<string, string>* InitTypesMap();
	
	ptrClashFunc findClashFunc(string nameObj1, string nameObj2);
	pair <string, string> makePair(string nameObj1, string nameObj2);
private:
	

};


class Main {

};

