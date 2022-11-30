#pragma once
#include <string>
#include <vector>
#include <iostream>
#include <filesystem>
#include "parser.hpp"

class Value
{
private:
	const std::string name;
	const std::string value;
public:
	Value(std::string name, std::string value) : name(name), value(value)
	{
		
	}
	
	std::string toString() const
	{
		std::stringstream ss;

		ss << "\"" << name << "\": " << "\"" << value << "\"";
		return ss.str();
	}
};

class Collection
{
private:
	const std::string name;
	const std::vector<std::string> values;
public:
	Collection(std::string name, std::vector<std::string> collection) : name(name), values(collection)
	{

	}
	std::vector<std::string> toString() const
	{
		std::vector<std::string> lines;
		lines.push_back(name);
		lines.push_back("[");
		for(int idx = 0; idx < values.size() - 1; ++idx)
			lines.push_back("\t\"" + values[idx] + "\",");
		if (values.size() > 0)
			lines.push_back("\t\"" + values[values.size() - 1] + "\"");
		lines.push_back("]");
		return lines;
	}
};

class JSON
{
private:
	std::vector<Value> values;
	std::vector<Collection> collections;

public:
	void addValue(std::string name, std::string value)
	{
		values.push_back(Value(name, value));
	}
	void addCollection(std::string name, std::vector<std::string> collection)
	{
		collections.push_back(Collection(name, collection));
	}

	std::vector<std::string> getLines() const
	{
		std::vector<std::string> lines;
		lines.push_back("{");

		for (int idx = 0; idx < values.size() - 1; ++idx)
			lines.push_back("\t" + values[idx].toString() + ",");
		if (collections.size() == 0)
		{
			if (values.size() > 0)
				lines.push_back("\t" + values[values.size() - 1].toString());
		}
		else
			lines.push_back("\t" + values[values.size() - 1].toString() + ",");



		for (int idx = 0; idx < collections.size() - 1; ++idx)
		{
			for (std::string l : collections[idx].toString())
				lines.push_back("\t" + l);
			lines[lines.size() - 1] += ",";
		}
		if (collections.size() > 0)
			for (std::string l : collections[collections.size() - 1].toString())
				lines.push_back("\t" + l);

		lines.push_back("}");

		return lines;
	}

	void writeToFile(std::string path)
	{
		Error pathErr = checkExtension(path, "json");
		if (pathErr != OK)
			return;

		std::ofstream os(path);

		std::vector<std::string> lines = getLines();

		for (std::string line : lines)
			os << line << std::endl;

		if (os.is_open())
			os.close();
	}
};
