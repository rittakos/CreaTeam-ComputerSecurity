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

struct Data
{
	std::string filePath;
	std::string creator;
	std::string creationTime;
	int animCount;
	int duration;
	int width;
	int height;

	void write(std::ostream& os) const;
};


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

	Error readFile();
	void reset();

	Error readHeader(unsigned long int size);
	Error readCredits(unsigned long int size);
	Error readAnimation(unsigned long int size);

	unsigned long long int allReadBytes = 0;

	Data data;

	void setData();

public:
	
	
	std::vector<AnimationBlock> animations;
	
	
	Error load(std::string path);
	std::string dataToString() const;
	~Caff();
};


#endif