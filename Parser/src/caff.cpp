#include "caff.h"
#include "parser.hpp"
#include <filesystem>

bool Caff::isPathValid(std::string path) const
{
	return true;
}

Error Caff::readFile()
{
	while (allReadBytes < fileSize)
	{
		std::optional<CaffBlockId> id = readBits<CaffBlockId>(is, 1);
		allReadBytes += 1;
		std::optional<unsigned long int> length = readBits<unsigned long int>(is, 8);
		allReadBytes += 8;

		if (!id.has_value())
			return Error(ErrorType::NoValue, "CAFF Block Id");
		if(!length.has_value())
			return Error(ErrorType::NoValue, "CAFF Block length");


		switch (id.value())
		{
			case CaffBlockId::Header :
			{
				Error err = readHeader(length.value());
				if (err != OK)
					return err;
				break;
			}
			case CaffBlockId::Credits:
			{
				Error err = readCredits(length.value());
				if (err != OK)
					return err;
				break;
			}
			case CaffBlockId::Animation:
			{
				Error err = readAnimation(length.value());
				if (err != OK)
					return err;
				break;
			}
			case CaffBlockId::Invalid:
			default:
			{
				reset();
				return ErrorType::InvalidBlockId;
			}
		}

		allReadBytes += length.value();
	}

	return ErrorType::OK;
}

void Caff::reset()
{

}

Error Caff::readHeader(unsigned long int size)
{
	int alreadyRead = 0;

	if (size != 20)
		return Error(ErrorType::CAFFHeaderError, "Wrong size!");

	{//magic
		std::optional<std::string> mag = readBits<std::string>(is, 4);
		alreadyRead += 4;
		if (mag.has_value())
			header.magic = mag.value();
		else
			return Error(ErrorType::NoValue, "CAFF Magic");

		if(header.magic != "CAFF")
			return Error(ErrorType::CAFFHeaderError, "Wrong magic value!");
	}

	{//hossz
		std::optional<unsigned long int> headerSize = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (headerSize.has_value() && headerSize.value() == 20)
			header.size = headerSize.value();
		else
			return Error(ErrorType::CAFFHeaderError, "Wrong size!");
	}

	{//anim count
		std::optional<unsigned long int> animCount = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (animCount.has_value())
			header.animCount = animCount.value();
		else
			return Error(ErrorType::CAFFHeaderError, "Wrong anim count!");
	}

	if (alreadyRead != size)
		return Error(ErrorType::CAFFHeaderError, "Incorrect number of bytes has been read!");

	return OK;
}

Error Caff::readCredits(unsigned long int size)
{
	//Credits

	int alreadyRead = 0;

	{//year
		std::optional<int> year = readBits<int>(is, 2);
		alreadyRead += 2;
		if (year.has_value())
			credits.year = year.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong year!");
	}

	{//month
		std::optional<int> month = readBits<int>(is, 1);
		alreadyRead += 1;
		if (month.has_value() && month.value() <= 12)
			credits.month = month.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong month!");
	}

	{//day
		std::optional<int> day = readBits<int>(is, 1);
		alreadyRead += 1;
		if (day.has_value() && day.value() <= 31)
			credits.day = day.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong day!");
	}

	{//hour
		std::optional<int> hour = readBits<int>(is, 1);
		alreadyRead += 1;
		if (hour.has_value() && hour.value() <= 24)
			credits.hour = hour.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong hour!");
	}

	{//minute
		std::optional<int> minute = readBits<int>(is, 1);
		alreadyRead += 1;
		if (minute.has_value() && minute.value() <= 60)
			credits.minute = minute.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong minute!");
	}

	{//creator length
		std::optional<unsigned long int> length = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (length.has_value())
			credits.creatorLength = length.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong creator length!");
	}

	{//creator
		std::optional<std::string> creator = readBits<std::string>(is, credits.creatorLength);
		alreadyRead += credits.creatorLength;
		if (creator.has_value())
			credits.creator = creator.value();
		else
			return Error(ErrorType::CAFFCreditsError, "Wrong creator!");
	}

	if (alreadyRead != size)
		return Error(ErrorType::CAFFCreditsError);

	return OK;
}

Error Caff::readAnimation(unsigned long int size)
{
	int alreadyRead = 0;

	AnimationBlock anim;

	{//duration
		std::optional<unsigned long int> dur = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (dur.has_value())
			anim.duration = dur.value();
		else
			return Error(ErrorType::CAFFAnimationError, "Wrong duration!");
	}

	{//ciff
		Error err = anim.ciff.read(is, size - 8);
		if (err != OK)
			return err;
		alreadyRead += size - 8;
	}

	if (alreadyRead != size)
		return Error(ErrorType::CAFFAnimationError);

	animations.push_back(anim);

	return ErrorType::OK;
}

Error Caff::load(std::string path)
{
	if (!isPathValid(path))
		return ErrorType::InvalidPath;
	
	this->path = path;


	try
	{
		fileSize = std::filesystem::file_size(this->path);
		is = std::make_shared<std::ifstream>(this->path, std::ifstream::binary);
	}
	catch (...)
	{
		is->close();
		return ErrorType::FileOpenError;
	}

	Error success = this->readFile();

	is->close();

	return success;
}

std::string Caff::dataToString() const
{
	return std::string();
}


Caff::~Caff()
{
	if (is != nullptr && is->is_open())
		is->close();
}
