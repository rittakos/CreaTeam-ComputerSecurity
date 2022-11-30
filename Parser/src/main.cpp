#include <iostream>
#include "caff.h"
#include "bitmap.h"
#include "error.h"
#include "parser.hpp"
#include "matadata.h"

//#include <crtdbg.h>
//
//#define _CRTDBG_MAP_ALLOC

std::ostream& os = std::cerr;

enum Mode { Data, Preview, None };

Error preview(const Caff& caff, std::optional<std::string> out)
{
	if (!out.has_value())
		return Error(InvalidPath, "No out file!!");

	Error pathErr = checkExtension(out.value(), "bmp");
	if (pathErr != OK)
		return pathErr;

	Bitmap bitmap;
	if (caff.animations.size() > 0)
	{
		Error err = bitmap.save(caff.animations[0].ciff, out.value());
		if (err == OK)
			os << "Bitmap generated succesfully!" << std::endl;
		else
			err.writeErrorMessage(os);
	}
	else
		os << "No ciff to generate preview!" << std::endl;

	return OK;
}

void data(const Caff& caff, std::optional<std::string> jsonPath = {})
{
	MetaData metaData = caff.getMetaData();
	if (!jsonPath.has_value())
	{
		metaData.write(std::cout);
		//std::string data = caff.dataToString();
		//std::cout << data;
	}
	else
	{
		metaData.createJSON().writeToFile(jsonPath.value());
	}
	
}

void processCaff(std::string filePath, Mode mode, std::optional<std::string> out = {})
{
	Caff caff;

	Error succes = caff.load(filePath);

	if (succes != ErrorType::OK)
	{
		succes.writeErrorMessage(os);
		return;
	}
	else
		os << "CAFF File has been read succesfully!" << std::endl;

	switch (mode)
	{
		case Data:
		{
			data(caff, out);
			break;
		}
		case Preview:
		{
			Error err = preview(caff, out);
			if (err != OK)
				err.writeErrorMessage(os);
			break;
		}
		case None:
		default:
		{
			os << "Invalid Mode!" << std::endl;
		}
	}
}


int main(int argc, char* argv[])
{
	Mode mode = None;
	if (argc < 3)
		mode = None;
	else if (argv[2] == std::string("data"))
		mode = Data;
	else if (argv[2] == std::string("preview"))
		mode = Preview;

	if (argc == 4)
		processCaff(argv[1], mode, argv[3]);
	else if (argc == 3)
		processCaff(argv[1], mode);
	else
		os << "Invalid Parameters!" << std::endl;

	//_CrtDumpMemoryLeaks();

	return 0;
}