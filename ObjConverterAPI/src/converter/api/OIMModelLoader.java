package converter.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import caesar.util.Vector2f;
import caesar.util.Vector3f;
import converter.api.model.IndexedModel;
import converter.api.model.IndexedVertex;

public class OIMModelLoader {

	public static IndexedModel loadModel(File file){
		IndexedModel model = null;
		try {
			model = load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return model;
	}

	private static IndexedModel load(File file) throws FileNotFoundException, XMLStreamException{
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader reader = xif.createXMLStreamReader(new FileInputStream(file));

		while(reader.hasNext()){
			int type = reader.next();
			switch(type){
			case XMLStreamReader.START_ELEMENT:
				if(reader.getLocalName().equals("model")){
					return readModel(reader);
				}
				break;
			case XMLStreamReader.END_ELEMENT:
				break;
			}
		}

		throw new XMLStreamException("Premature end of file");
	}

	private static IndexedModel readModel(XMLStreamReader reader) throws XMLStreamException{
		IndexedModel model = new IndexedModel();
		
		while(reader.hasNext()){
			int type = reader.next();
			switch(type){
			case XMLStreamReader.START_ELEMENT:
				String localName = reader.getLocalName();
				if(localName.equals("sections")){
					readSections(reader, model);
				}
				else if(localName.equals("vertices")){
					readVertices(reader, model);
				}
				else if(localName.equals("indices")){
					readIndices(reader, model);
				}
				break;
				
			case XMLStreamReader.END_ELEMENT:
				return model;
			}
		}
		throw new XMLStreamException("Premature end of file");
	}
	
	private static void readVertices(XMLStreamReader reader, IndexedModel model) throws XMLStreamException{
		while(reader.hasNext()){
			int type = reader.next();
			switch(type){
			case XMLStreamReader.START_ELEMENT:
				model.addVertex(readVertex(reader));
				break;
			case XMLStreamReader.END_ELEMENT:
				return;
			}
		}
	}
	
	private static IndexedVertex readVertex(XMLStreamReader reader) throws XMLStreamException{
		String indexVal = reader.getAttributeValue(null, "index");
		int index = Integer.parseInt(indexVal);
		String data = readCharacters(reader);
		String[] values = data.split(" ");
		Vector3f pos = new Vector3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
		Vector2f uv = new Vector2f(Float.parseFloat(values[3]), Float.parseFloat(values[4]));
		Vector3f norm = new Vector3f(Float.parseFloat(values[5]), Float.parseFloat(values[6]), Float.parseFloat(values[7]));
		return new IndexedVertex(index, pos, uv, norm);
	}
	
	private static void readIndices(XMLStreamReader reader, IndexedModel model) throws XMLStreamException{
		String data = readCharacters(reader);
		String[] values = data.split(" ");
		for(int i = 0; i < values.length; i++){
			String value = values[i];
			int index = Integer.parseInt(value);
			model.addIndex(index);
		}
	}
	
	private static void readSections(XMLStreamReader reader, IndexedModel model) throws XMLStreamException {
		while(reader.hasNext()){
			int type = reader.next();
			switch(type){
			case XMLStreamReader.START_ELEMENT:
				String elementName = reader.getLocalName();
				if(elementName.equals("section")){
					readSection(reader, model);
				}
				break;
				
			case XMLStreamReader.END_ELEMENT:
				return;
			}
		}
	}
	
	private static void readSection(XMLStreamReader reader, IndexedModel model) throws XMLStreamException{
		String id = reader.getAttributeValue(null, "id");
		int start = 0;
		int count = 0;
		while(reader.hasNext()){
			int type = reader.next();
			switch(type){
			case XMLStreamReader.START_ELEMENT:
				String elementName = reader.getLocalName();
				if(elementName.equals("start")){
					start = readInt(reader);
				}
				if(elementName.equals("count")){
					count = readInt(reader);
				}
				break;
				
			case XMLStreamReader.END_ELEMENT:
				model.createSection(id, start, count);
				return;
			}
		}
	}

	private static String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
     
    private static int readInt(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Integer.valueOf(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + characters);
        }
    }
}
