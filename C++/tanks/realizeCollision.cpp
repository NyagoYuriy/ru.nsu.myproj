#include "prog.h"

pair <int, int> RealizeCollizion::MakePair(int x, int y) {
	pair <int, int> pair;
	pair.first = x;
	pair.second = y;
	return pair;
}

bool RealizeCollizion::DotInTheBox(pair <int, int> point, pair <int, int> upperLeft, pair <int, int> rightBottom) {
	if (point.first >= upperLeft.first && point.first <= rightBottom.first)
		if (point.second >= rightBottom.second && point.second <= upperLeft.second)
			return true;
	return false;
}

pair<int, int> RealizeCollizion::CheckCollizion(Collizianable& obj1, Collizianable& obj2) {
	pair <int, int> upperLeft1, upperRight1, leftBottom1, upperRight2, leftBottom2, upperLeft2, rightBottom1, rightBottom2;
	if (obj1.Name() == "wall" && obj2.Name() == "wall")
		return MakePair(0, 0);
	upperLeft1.first = obj1.coordinates.first - obj1.sizeX;
	upperLeft1.second = obj1.coordinates.second + obj1.sizeY;

	upperLeft2.first = obj2.coordinates.first - obj2.sizeX;
	upperLeft2.second = obj2.coordinates.second + obj2.sizeY;

	rightBottom1.first = obj1.coordinates.first + obj1.sizeX;
	rightBottom1.second = obj1.coordinates.second - obj1.sizeY;

	rightBottom2.first = obj2.coordinates.first + obj2.sizeX;
	rightBottom2.second = obj2.coordinates.second - obj2.sizeY;

	leftBottom1.first = upperLeft1.first;
	leftBottom1.second = rightBottom1.second;

	upperRight1.first = rightBottom1.first;
	upperRight1.second = upperLeft1.second;

	
	leftBottom2.first = upperLeft2.first;
	leftBottom2.second = rightBottom2.second;

	upperRight2.first = rightBottom2.first;
	upperRight2.second = upperLeft2.second;



	if (DotInTheBox(upperLeft1, upperLeft2, rightBottom2)) {
		if (DotInTheBox(leftBottom1, upperLeft2, rightBottom2))
			return MakePair(abs(upperLeft1.first - upperRight2.first), 0);
		if (DotInTheBox(upperRight1, upperLeft2, rightBottom2))
			return MakePair(0, abs(upperLeft1.second - rightBottom2.second));
		return MakePair(abs(upperLeft1.first - upperRight2.first), abs(upperLeft1.second - rightBottom2.second));
	}
	if (DotInTheBox(upperRight1, upperLeft2, rightBottom2)) {
		if (DotInTheBox(rightBottom1, upperLeft2, rightBottom2))
			return MakePair(abs(upperRight1.first - upperLeft2.first), 0);
		return MakePair(abs(upperRight1.first - upperLeft2.first), abs(upperRight1.second - leftBottom2.second));
	}
	if (DotInTheBox(rightBottom1, upperLeft2, rightBottom2)) {
		if ((DotInTheBox(leftBottom1, upperLeft2, rightBottom2)))
			return MakePair(0, abs(leftBottom1.second - upperRight2.second));
		return MakePair(abs(rightBottom1.first - upperLeft2.first), abs(leftBottom1.second - upperRight2.second));
	}
	if (DotInTheBox(leftBottom1, upperLeft2, rightBottom2)) {
		return MakePair(abs(leftBottom1.first - upperRight2.first), abs(leftBottom1.second - upperRight2.second));
	}
	return MakePair(0, 0);
}


//End COlliz


RealizeCollizion::RealizeCollizion() {
	//mapType = *InitTypesMap();
	mapCollizion = *initClashMap();
}

void RealizeCollizion::TankShot(Collizianable& _tank, Collizianable& _shot, pair <int, int> delta) {
	auto& tank = dynamic_cast<Tank&>(_tank);
	auto& shot = dynamic_cast<Shot&>(_shot);
	tank.health--;
	shot.health--;
}


void RealizeCollizion::TankWall(Collizianable& _tank, Collizianable& _wall, pair <int, int> delta) {
	auto& tank = dynamic_cast<Tank&>(_tank);
	auto& wall = dynamic_cast<Wall&>(_wall);
	tank.PushOut(delta);
}

void RealizeCollizion::ShotWall(Collizianable& _shot, Collizianable& _wall, pair <int, int> delta) {
	auto& shot = dynamic_cast<Shot&>(_shot);
	auto& wall = dynamic_cast<Wall&>(_wall);
	wall.health--;
	shot.health--;
}

void RealizeCollizion::TankTank(Collizianable& _tank1, Collizianable& _tank2, pair <int, int> delta) {
	auto& tank1 = dynamic_cast<Tank&>(_tank1);
	auto& tank2 = dynamic_cast<Tank&>(_tank2);
	int tmp = tank2.health;
	tank2.health -= tank1.health;
	tank1.health -= tmp;
}

void RealizeCollizion::ShotShot(Collizianable& _shot1, Collizianable& _shot2, pair <int, int> delta) {
	auto& shot1 = dynamic_cast<Shot&>(_shot1);
	auto& shot2 = dynamic_cast<Shot&>(_shot2);
	int tmp = shot2.health;
	shot2.health -= shot1.health;
	shot1.health -= tmp;
}

//vice verse
void RealizeCollizion::ShotTank(Collizianable& shot, Collizianable& tank, pair <int, int> delta) {
	TankShot(tank, shot, delta);
}

void RealizeCollizion::WallTank(Collizianable& wall, Collizianable& tank, pair <int, int> delta) {
	TankWall(tank, wall, delta);
}

void RealizeCollizion::WallShot(Collizianable& wall, Collizianable& shot, pair <int, int> delta) {
	ShotWall(shot, wall, delta);
}

void RealizeCollizion::UnknownClash(Collizianable& obj1, Collizianable& obj2, pair <int, int> delta) {
	throw ("Unknown obj");
}


pair <string, string> RealizeCollizion::makePair(string nameObj1, string nameObj2) {
	pair <string, string> pair;
	pair.first = nameObj1;
	pair.second = nameObj2;
	return pair;
}


clashMap* RealizeCollizion::initClashMap() {
	clashMap* ptrMap = new clashMap;
	(*ptrMap)[makePair("tank", "shot")] = &RealizeCollizion::TankShot;

	(*ptrMap)[makePair("tank", "wall")] = &RealizeCollizion::TankWall;
	(*ptrMap)[makePair("shot", "wall")] = &RealizeCollizion::ShotWall;
	(*ptrMap)[makePair("tank", "tank")] = &RealizeCollizion::TankTank;
	(*ptrMap)[makePair("shot", "shot")] = &RealizeCollizion::ShotShot;

	(*ptrMap)[makePair("wall", "tank")] = &RealizeCollizion::WallTank;
	(*ptrMap)[makePair("shot", "tank")] = &RealizeCollizion::ShotTank;
	(*ptrMap)[makePair("wall", "shot")] = &RealizeCollizion::WallShot;

	return ptrMap;
}
/*
map<string, string>* RealizeCollizion::InitTypesMap() {
	std::map<std::string, std::string>* type = new std::map<std::string, std::string>;
	Tank tank;
	Wall wall;
	Shot shot;
	(*type)[typeid(tank).name()] = "tank";
	(*type)[typeid(wall).name()] = "wall";
	(*type)[typeid(shot).name()] = "shot";
	return type;
}
*/
ptrClashFunc RealizeCollizion::findClashFunc(string nameObj1, string nameObj2) {
	return mapCollizion[makePair(nameObj1, nameObj2)];
}




