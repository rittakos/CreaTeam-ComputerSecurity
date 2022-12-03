#pragma once
#include <string>
#include <vector>
#include <iostream>
#include <filesystem>
#include <fstream>
#include "json.h"

struct MetaData
{
	std::string					fileName;
	std::string					creator;
	std::string					creationTime;
	int							animCount;
	int							duration;
	int							width;
	int							height;

	std::vector<std::string>	tags;
	std::vector<std::string>	captions;

	JSON createJSON()
	{
		JSON json;

		json.addValue("File", fileName);
		json.addValue("Creator", creator);
		json.addValue("Creation Time", creationTime);
		json.addValue("Animation Count", std::to_string(animCount));
		json.addValue("Duration", std::to_string(duration));
		json.addValue("Width", std::to_string(width));
		json.addValue("Height", std::to_string(height));
		
		json.addCollection("Captions", captions);
		json.addCollection("Tags", tags);

		return json;
	}

	std::string write(std::ostream& os) const
	{
		std::stringstream ss;

		ss << fileName << std::endl;
		ss << creator << std::endl;
		ss << creationTime << std::endl;
		ss << animCount << std::endl;
		ss << duration << std::endl;
		ss << width << std::endl;
		ss << height << std::endl;
		for (std::string caption : captions)
			ss << caption << ", ";
		ss << std::endl;
		for (std::string tag : tags)
			ss << tag << ", ";
		ss << std::endl;


		os << ss.str();
		return ss.str();
	}
};