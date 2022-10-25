#ifndef PARSER_H
#define PARSER_H

#include <iostream>
#include <optional>
#include <fstream>
#include <memory>
#include "pixel.h"

enum CaffBlockId { Header, Credits, Animation, Invalid };

static Error checkExtension(std::string path, std::string extension)
{
	std::string delimiter = ".";
	std::vector<std::string> parts;

	size_t pos = 0;
	std::string part;
	while ((pos = path.find(delimiter)) != std::string::npos) {
		part = path.substr(0, pos);
		parts.push_back(part);
		path.erase(0, pos + delimiter.length());
	}
	parts.push_back(path);

	if (path == extension)
		return OK;

	return Error(InvalidPath, "Bad extension!");
}

static bool convert(CaffBlockId& result, char* from, size_t size)
{
	if (size != 1 || from == nullptr)
		return false;
	int id = (int)from[0];
	CaffBlockId resId;
	if (id == 1)
		resId = CaffBlockId::Header;
	else if (id == 2)
		resId = CaffBlockId::Credits;
	else if (id == 3)
		resId = CaffBlockId::Animation;
	else {
		resId = CaffBlockId::Invalid;
		return false;
	}

	result = resId;

	return true;
}

static bool convert(unsigned long int& result, char* from, size_t size)
{
	if (from == nullptr || size > 8)
		return false;
	
	unsigned long int value = 0;

	for (int i = size - 1; 0 <= i; --i) {
		value = value << 8;
		value += (unsigned char)from[i];
	}

	result = value;

	return true;
}

static bool convert(int& result, char* from, size_t size)
{
	if (from == nullptr)
		return false;

	int value = 0;

	for (int i = size - 1; 0 <= i; --i) {
		value = value << 8;
		value += (unsigned char)from[i];
	}

	result = value;

	return true;
}

static bool convert(std::string& result, char* from, size_t size)
{
	if (from == nullptr)
		return false;

	std::string value = "";

	for (int idx = 0; idx < size; ++idx)
		value += from[idx];

	result = value;

	return true;
}

static bool convert(char& result, char* from, size_t size)
{
	if (from == nullptr || size != 1)
		return false;

	result = *from;

	return true;
}

static bool convert(Pixel& result, char* from, size_t size)
{
	if (from == nullptr || size != 3)
		return false;

	std::byte R, G, B;

	R = (std::byte) from[0];
	G = (std::byte) from[1];
	B = (std::byte) from[2];

	result = Pixel((int)R, (int)G, (int)B);

	return true;
}

template <class T>
concept Readable =	std::same_as<T, CaffBlockId> || 
					std::same_as<T, unsigned long int> ||
					std::same_as<T, std::string> ||
					std::same_as<T, int> ||
					std::same_as<T, char> ||
					std::same_as<T, Pixel> ;


template<Readable T>
std::optional<T> readBits(std::shared_ptr<std::ifstream> is, int size)
{
	T result;
	bool success;

	try
	{
		char* input = new char[size];
		is->read(input, size);

		success = convert(result, input, size);

		delete[] input;
	}
	catch (...)
	{
		std::cout << "Problem while reading!" << std::endl;
		return {};
	}

	if (!success)
		return {};

	return result;
}

#endif