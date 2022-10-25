#ifndef BITMAP_H
#define BITMAP_H


#include <cstdint>
#include <fstream>


class Bitmap
{
private:
    unsigned short type = 0x4d42; //BM

	struct {
        unsigned int   size;           
        unsigned short reserved1    = 0; 
        unsigned short reserved2    = 0;
        unsigned int   offset;
	} header;

    struct {
        unsigned int   size                 = 40;           
        int            width;          
        int            height;         
        unsigned short colorPlanes          = 1;  
        unsigned short depth                = 24;       
        unsigned int   compression          = 0;
        unsigned int   imageSize            = 0;      
        int            horizontalResolution = 0;
        int            verticalResolution   = 0; 
        unsigned int   colorsNum            = 0;
        unsigned int   importantColors      = 0;
    } infoHeader;



    bool createBitmap(std::ofstream& os, const Ciff& ciff) {
        
        unsigned int height = ciff.header.height;
        unsigned int width = ciff.header.width;

        header.size = sizeof(type) + sizeof(header) + sizeof(infoHeader) + width * height * sizeof(Pixel);
        header.offset = sizeof(type) + sizeof(header) + sizeof(infoHeader);

        if (sizeof(infoHeader) != infoHeader.size)
            return false;

        infoHeader.width = width;
        infoHeader.height = height;

        os.write((char*)&type, sizeof(type));
        os.write((char*)&header, sizeof(header));
        os.write((char*)&infoHeader, sizeof(infoHeader));

        for (int i = (height * width) - 1; i >= 0; --i)
        {
            int R = ciff.pixels[i].R;
            int G = ciff.pixels[i].G;
            int B = ciff.pixels[i].B;
            os.write((char*)&B, 1);
            os.write((char*)&G, 1);
            os.write((char*)&R, 1);
        }
        

        return true;
    }


public:
    Error save(const Ciff& ciff, std::string path)
    {
        std::ofstream os(path, std::ios::binary);

        createBitmap(os, ciff);

        if(!os && os.is_open())
            os.close();

        return OK;
    }
};

#endif