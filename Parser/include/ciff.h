#pragma once
#include <string>
#include <vector>
#include "pixel.h"
#include <memory>
#include "error.h"


class Ciff
{
public:
	struct
	{
		std::string magic;
		unsigned long int size;
		unsigned long int contentSize;
		unsigned long int width;
		unsigned long int height;
		std::string caption;
		std::vector<std::string> tags;
	} header;

	std::vector<Pixel> pixels;

private:
	bool valid;
	unsigned long int blockSize;

	Error readBlocks(std::shared_ptr<std::ifstream> is, unsigned long int size);
public:

	Error read(std::shared_ptr<std::ifstream> is, unsigned long int size);
	bool isValid() const { return valid; }

	Ciff() = default;
	~Ciff();
};