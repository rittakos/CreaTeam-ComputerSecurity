#ifndef CAFF_H
#define CAFF_H

#include <iostream>
#include <fstream>
#include "ciff.h"
#include "error.h"

struct AnimationBlock
{
	unsigned long int duration = 0;
	Ciff ciff;

	AnimationBlock() = default;
};

enum CaffBlockId {Header, Credits, Animation, Invalid};

class Caff
{
	std::string						path;
	std::shared_ptr<std::ifstream>	is;

	struct
	{
		std::string magic;
		unsigned long int size;
		unsigned long int animCount;
	} header;


	struct
	{
		int year;
		int month;
		int day;
		int hour;
		int minute;

		unsigned long int creatorLength;
		std::string creator;
	} credits;

	unsigned long long fileSize = 0;

	bool isPathValid(std::string path) const;
	Error readFile();
	void reset();

	Error readHeader(unsigned long int size);
	Error readCredits(unsigned long int size);
	Error readAnimation(unsigned long int size);

	unsigned long long int allReadBytes = 0;

public:
	
	
	std::vector<AnimationBlock> animations;
	
	
	Error load(std::string path);
	std::string dataToString() const;
	~Caff();
};


#endif