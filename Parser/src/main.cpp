#include <iostream>
#include "caff.h"
#include "bitmap.h"
#include "error.h"



int main(int argc, char* argv[])
{
	Caff caff;
	Error succes = caff.load(argv[1]);

	if (succes != ErrorType::OK)
	{
		succes.writeErrorMessage(std::cout);
		return 0;
	}
	else
		std::cout << "CAFF File has been read succesfully!" << std::endl;
	
	if (argv[2] == std::string("preview"))
	{
		Bitmap bitmap;
		Error err = bitmap.save(caff.animations[0].ciff, "C:\\Projects\\KreaTeam-ComputerSecurity\\ExampleFiles\\out.bmp");
		if (err == OK)
			std::cout << "Bitmap generated succesfully!" << std::endl;
		else
			err.writeErrorMessage(std::cout);
	}
	else if (argv[2] == "data")
	{
		std::string data = caff.dataToString();
		std::cout << data;
	}
}