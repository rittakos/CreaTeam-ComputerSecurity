#include "ciff.h"
#include "parser.hpp"
#include <optional>
#include <iostream>

Error Ciff::readBlocks(std::shared_ptr<std::ifstream> is, unsigned long int size)
{
	unsigned long int alreadyRead = 0;

	{//magic
		std::optional<std::string> mag = readBits<std::string>(is, 4);
		alreadyRead += 4;
		if (mag.has_value())
			header.magic = mag.value();
		else
			return Error(ErrorType::NoValue, "CIFF Magic");
			
		if (header.magic != "CIFF")
			return Error(ErrorType::CIFFError, "Wrong Magic Value!");
	}

	{//size
		std::optional<unsigned long int> headerSize = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (headerSize.has_value())
			header.size = headerSize.value();
		else
			return Error(ErrorType::NoValue, "CIFF Size");
	}

	{//content size
		std::optional<unsigned long int> contentSize = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (contentSize.has_value())
			header.contentSize = contentSize.value();
		else
			return Error(ErrorType::NoValue, "CIFF Content Size!");
	}

	if (header.contentSize + header.size != size)
		return Error(ErrorType::CIFFError, "Size mismatch!");

	{//width
		std::optional<unsigned long int> w = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (w.has_value())
			header.width = w.value();
		else
			return Error(ErrorType::NoValue, "CIFF Width");
	}

	{//height
		std::optional<unsigned long int> h = readBits<unsigned long int>(is, 8);
		alreadyRead += 8;
		if (h.has_value())
			header.height = h.value();
		else
			return Error(ErrorType::NoValue, "CIFF Height");
	}

	if (header.width * header.height * 3 != header.contentSize)
		return Error(ErrorType::CIFFError, "Content Size Mismatch!");

	{//caption
		header.caption = "";

		while (true)
		{
			std::optional<char> currentChar = readBits<char>(is, 1);
			if(!currentChar.has_value())
				return Error(ErrorType::NoValue, "Caption char!");

			if (currentChar.value() == '\n')
				break;

			header.caption += currentChar.value();
		}

		alreadyRead += header.caption.size() + 1;
	}

	{//tags
		char currentChar;
		std::string currentTag = "";

		while (alreadyRead < header.size)
		{
			std::optional<char> current = readBits<char>(is, 1);
			if (!current.has_value())
				return Error(ErrorType::NoValue, "CIFF tag char");

			currentChar = current.value();
			++alreadyRead;

			if (currentChar == '\0')
			{
				header.tags.push_back(currentTag);
				currentTag = "";
				continue;
			}

			currentTag += currentChar;
		}
	}

	if (alreadyRead != header.size)
		return Error(ErrorType::CIFFError, "Incorrect number of bytes has been read!");

	{//pixels
		for (size_t idx = 0; idx < header.contentSize / 3; ++idx)
		{
			std::optional<Pixel> currentPixel = readBits<Pixel>(is, 3);
			if (!currentPixel.has_value()) 
				return Error(ErrorType::NoValue, "Pixel!");
				
			Pixel pixel = currentPixel.value();

			if(!pixel.isValid())
				return Error(ErrorType::CIFFError, "Invalid Pixel");

			alreadyRead += 3;
			pixels.push_back(pixel);
		}
	}

	if (alreadyRead != size)
		return Error(ErrorType::CIFFError, "Incorrect number of bytes has been read!");

	return ErrorType::OK;
}

Error Ciff::read(std::shared_ptr<std::ifstream> is, unsigned long int size)
{
	try
	{
		Error result = readBlocks(is, size);
		valid = result == OK;
		
		return result;
	}
	catch (...)
	{
		return Error(ErrorType::UnhandledException, "CIFF read");
	}
}


Ciff::~Ciff()
{

}
