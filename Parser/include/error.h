#ifndef ERROR_H
#define ERROR_H

#include <ostream>
#include <map>
#include <optional>

enum ErrorType{	OK, 
				InvalidPath, 
				FileOpenError, 
				NoValue, 
				InvalidBlockId, 
				CAFFHeaderError,
				CAFFCreditsError,
				CAFFAnimationError,
				CIFFError,
				ByteReadError,
				UnhandledException,
				InvalidParameter};

class Error
{
	ErrorType type;
	const std::map<ErrorType, std::string> messages =
						{
							{ErrorType::OK,					"OK"},
							{ErrorType::InvalidPath,		"Invalid Pat!"},
							{ErrorType::FileOpenError,		"Error while opening file!"},
							{ErrorType::NoValue,			"Read result has no value!"},
							{ErrorType::InvalidBlockId,		"Invalid Block Id!"},
							{ErrorType::CAFFHeaderError,	"Error while reading CAFF header!"},
							{ErrorType::CAFFCreditsError,	"Error while reading CAFF credits!"},
							{ErrorType::CAFFAnimationError, "Error while reading CAFF animation!"},
							{ErrorType::CIFFError,			"Error while reading CIFF!"},
							{ErrorType::ByteReadError,		"Error while reading Bytes!"},
							{ErrorType::UnhandledException,	"Unhandled exception!"},
							{ErrorType::InvalidParameter,	"Invalid input parameter!"}
						};
	std::optional<std::string> massage;
public:
	Error(ErrorType type) : type (type) 
	{
		massage = {};
	}

	Error(ErrorType type, std::string massage) : type(type)
	{
		this->massage = massage;
	}

	bool operator==(Error other) const
	{
		return this->type == other.type;
	}

	bool operator!=(Error other) const
	{
		return !((*this) == other);
	}

	void writeErrorMessage(std::ostream& os) const
	{
		if (messages.contains(type))
			os << messages.at(type);
		else
		{
			os << "Wrong error type!" << std::endl;
			return;
		}

		if(massage.has_value())
			os << "  -  " << massage.value() << std::endl;
	}
};

#endif