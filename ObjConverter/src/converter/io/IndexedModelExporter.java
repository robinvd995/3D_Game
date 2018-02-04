package converter.io;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import converter.api.model.Face;
import converter.api.model.IndexedModel;
import converter.api.model.IndexedVertex;
import converter.api.model.ModelSection;

public class IndexedModelExporter {

	private Document document;
	private File file;
	
	public IndexedModelExporter(File file){
		this.file = file;
	}
	
	public void exportModel(IndexedModel model) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		document = builder.newDocument();
		
		Element root = document.createElement("model");
		document.appendChild(root);
		
		root.appendChild(createVerticesElement(model));
		root.appendChild(createIndicesElement(model));
		root.appendChild(createSectionsElement(model));
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}
	
	private Element createVerticesElement(IndexedModel model){
		Element verticesElement = document.createElement("vertices");
		verticesElement.setAttribute("size", String.valueOf(model.getVertexCount()));
		for(IndexedVertex vertex : model.getVertices()){
			verticesElement.appendChild(createVertexNode(vertex));
		}
		return verticesElement;
	}
	
	private Element createIndicesElement(IndexedModel model){
		List<Integer> indices = model.getIndices();
		String indicesString = "";
		for(int i = 0; i < indices.size(); i++){
			indicesString = indicesString + indices.get(i) + (i + 1 < indices.size() ? " " : "");
		}
		Element indicesElement = getElement("indices", indicesString);
		indicesElement.setAttribute("size", String.valueOf(model.getIndicesCount()));
		return indicesElement;
	}
	
	private Element createSectionsElement(IndexedModel model){
		Element sectionsElement = document.createElement("sections");
		
		for(String s : model.getAllSections()){
			ModelSection section = model.getSection(s);
			Element sectionElement = document.createElement("section");
			sectionElement.setAttribute("id", s);
			sectionElement.appendChild(getElement("start", String.valueOf(section.getStart())));
			sectionElement.appendChild(getElement("count", String.valueOf(section.getCount())));
			sectionsElement.appendChild(sectionElement);
		}
		
		return sectionsElement;
	}
	
	private Node createVertexNode(IndexedVertex vertex){
		String vertexData = vertex.position.getX() + " " + vertex.position.getY() + " " + vertex.position.getZ() + " " + 
							vertex.uv.getX() + " " + vertex.uv.getY() + " " +
							vertex.normal.getX() + " " + vertex.normal.getY() + " " + vertex.normal.getZ();
		Element vertexElement = getElement("vertex", vertexData);
		vertexElement.setAttribute("index", String.valueOf(vertex.id));		
		return vertexElement;
	}
	
	private Element getElement(String name, String value){
		Element node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		return node;
	}
}
