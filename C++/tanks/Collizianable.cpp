#include "prog.h"

pair <int, int> Collizianable::MakePair(int x, int y) {
	pair <int, int> pair;
	pair.first = x;
	pair.second = y;
	return pair;
}
/*
bool Collizianable::DotInTheBox(pair <int, int> point, pair <int, int> upperLeft, pair <int, int> rightBottom) {
	if (point.first >= upperLeft.first && point.first <= rightBottom.first)
		if (point.second >= rightBottom.second && point.second <= upperLeft.second)
			return true;
	return false;
}

pair<int, int> Collizianable::CheckCollizion(Collizianable& obj1, Collizianable& obj2) {
	pair <int, int> upperLeft1, upperRight1, leftBottom1, upperRight2, leftBottom2, upperLeft2, rightBottom1, rightBottom2;

	upperLeft1.first = obj1.coordinates.first - obj1.sizeX;
	upperLeft1.second = obj1.coordinates.second + obj1.sizeY;

	leftBottom1.first = upperLeft1.first;
	leftBottom1.second = rightBottom1.second;

	upperRight1.first = rightBottom1.first;
	upperRight1.second = upperLeft1.second;

	upperLeft2.first = obj2.coordinates.first - obj2.sizeX;
	upperLeft2.second = obj2.coordinates.second + obj2.sizeY;

	rightBottom1.first = obj1.coordinates.first + obj1.sizeX;
	rightBottom1.second = obj1.coordinates.second - obj1.sizeY;

	rightBottom2.first = obj2.coordinates.first + obj2.sizeX;
	rightBottom2.second = obj2.coordinates.second - obj2.sizeY;

	leftBottom2.first = upperLeft2.first;
	leftBottom2.second = rightBottom2.second;

	upperRight2.first = rightBottom2.first;
	upperRight2.second = upperLeft2.second;



	if (DotInTheBox(upperLeft1, upperLeft2, rightBottom2)) {
		if (DotInTheBox(leftBottom1, upperLeft2, rightBottom2))
			return MakePair(abs(upperLeft1.first - upperRight2.first) + 1, 0);
		if (DotInTheBox(upperRight1, upperLeft2, rightBottom2))
			return MakePair(0, abs (upperLeft1.second - rightBottom2.second) + 1);
		return MakePair(abs(upperLeft1.first - upperRight2.first) + 1, abs(upperLeft1.second - rightBottom2.second) + 1);
	}
	if (DotInTheBox(upperRight1, upperLeft2, rightBottom2)) {
		if (DotInTheBox(rightBottom1, upperLeft2, rightBottom2))
			return MakePair(abs(upperRight1.first - upperLeft2.first) + 1, 0);
		return MakePair(abs(upperRight1.first - upperLeft2.first) + 1, abs(upperRight1.second - leftBottom2.second) + 1);
	}
	if (DotInTheBox(rightBottom1, upperLeft2, rightBottom2)) {
		if ((DotInTheBox(leftBottom1, upperLeft2, rightBottom2)))
			return MakePair(0, abs (leftBottom1.second - upperRight2.second) + 1);
		return MakePair(abs (rightBottom1.first - upperLeft2.first) + 1,abs(leftBottom1.second - upperRight2.second) + 1);
	}
	if (DotInTheBox(leftBottom1, upperLeft2, rightBottom2)) {
		return MakePair(abs(leftBottom1.first - upperRight2.first), abs (leftBottom1.second - upperRight2.second));
	}
	return MakePair(0, 0);
}
	*/