package model.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.paint.Color;
import model.levels.util.ColorMap;

public class XmlSaver implements SaverIF {

    private static XmlSaver saver;
    private final ColorMap colorMap;

    private XmlSaver() {
        colorMap = new ColorMap();
    }

    public static SaverIF getInstance() {
        if (saver == null) {
            saver = new XmlSaver();
        }
        return saver;
    }

    @Override
    public boolean save(final Snapshot saved, final String path, final String name) {

        try {
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            final Document doc = dBuilder.newDocument();
            // root element
            final Element rootElement = doc.createElement(name);
            doc.appendChild(rootElement);
            /************* Game State *********************/
            saveGameState(doc, rootElement, saved);
            /********************************************/
            /*************** Stacks ***********************/
            saveGameStacks(doc, rootElement, saved);
            /********************************************/
            // write the content into xml file
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(new File(path + File.separator + name + ".xml"));
            transformer.transform(source, result);
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void saveGameState(final Document doc, final Element rootElement, final Snapshot saved) {
        final Element GameState = doc.createElement("GameState");
        rootElement.appendChild(GameState);

        final Element diff = doc.createElement("Difficulty");
        diff.appendChild(doc.createTextNode(Integer.toString(saved.getGameState().getDiff())));
        GameState.appendChild(diff);

        final Element time = doc.createElement("Time");
        time.appendChild(doc.createTextNode(Integer.toString(saved.getGameState().getElapsedTime())));
        GameState.appendChild(time);

        final Element players = doc.createElement("players");
        for (int i = 0; i < saved.getGameState().getPsLength(); i++) {
            final Element player = doc.createElement("player");
            players.appendChild(player);
            final Element x = doc.createElement("x");
            player.appendChild(x);
            x.appendChild(doc.createTextNode(Integer.toString(saved.getGameState().getP(i)[0])));
            final Element y = doc.createElement("y");
            player.appendChild(y);
            y.appendChild(doc.createTextNode(Integer.toString(saved.getGameState().getP(i)[1])));

        }
        GameState.appendChild(players);
        final Element scores = doc.createElement("Scores");
        for (int i = 0; i < saved.getGameState().getScores().length; i++) {
            final Element score = doc.createElement("Score");
            score.appendChild(doc.createTextNode(Integer.toString(saved.getGameState().getScores()[i])));
            scores.appendChild(score);
        }
        GameState.appendChild(scores);
    }

    private void saveGameStacks(final Document doc, final Element rootElement, final Snapshot saved) {
        final Element stacks = doc.createElement("Stacks");
        rootElement.appendChild(stacks);
        for (int i = 0; i < saved.getDateSize(); i++) {
            final Element stack = doc.createElement("Stack");
            //// Right hand
            final Element right = doc.createElement("right");
            final PlayersStacksData data = saved.getDate(i);
            saveHands(doc, right, data.getRightHandshapes());

            final Element left = doc.createElement("left");
            saveHands(doc, left, data.getlefttHandshapes());
            //// Left Hand
            // End
            stack.appendChild(right);
            stack.appendChild(left);
            stacks.appendChild(stack);

        }
    }

    private void saveHands(final Document doc, final Element rootElement, final ArrayList<SaveShapeNode> saved) {

        for (int i = 0; i < saved.size(); i++) {
            final Element element = doc.createElement("element");
            // x y name color
            final Element x = doc.createElement("x");
            x.appendChild(doc.createTextNode(Integer.toString((int) saved.get(i).getX())));
            final Element y = doc.createElement("y");
            y.appendChild(doc.createTextNode(Integer.toString((int) saved.get(i).getY())));
            final Element color = doc.createElement("color");
            color.appendChild(doc.createTextNode((saved.get(i).getColor()).toString()));
            final Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(saved.get(i).getName()));
            element.appendChild(name);
            element.appendChild(color);
            element.appendChild(x);
            element.appendChild(y);
            rootElement.appendChild(element);
        }
    }

    @Override
    public Snapshot load(final String path, final String name) {

        final Snapshot load = new Snapshot();
        SavedStates g;
        final PlayersStacksData[] data = new PlayersStacksData[2];

        final String fullPath = path + File.separator + name + ".xml";
        try {
            final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = builderFactory.newDocumentBuilder();
            final File inputFile = new File(fullPath);
            final InputStream inputStream = new FileInputStream(inputFile);
            final org.w3c.dom.Document doc = builder.parse(fullPath);
            doc.getDocumentElement().normalize();
            final int diff = Integer.valueOf(doc.getElementsByTagName("Difficulty").item(0).getTextContent());
            final int time = Integer.valueOf(doc.getElementsByTagName("Time").item(0).getTextContent());
            final int[] scores = new int[] {-1, -1};
            NodeList nList = doc.getElementsByTagName("Score");
            for (int i = 0; i < nList.getLength(); i++) {
                final Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;
                    scores[i] = Integer.valueOf(eElement.getTextContent());
                }
            }
            final int Ps[][] = new int[2][];
            nList = doc.getElementsByTagName("player");
            for (int i = 0; i < nList.getLength(); i++) {
                final int[] p = new int[2];
                final Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;
                    p[0] = Integer.valueOf(eElement.getElementsByTagName("x").item(0).getTextContent());
                    p[1] = Integer.valueOf(eElement.getElementsByTagName("y").item(0).getTextContent());
                }
                Ps[i] = p;
            }
            g = new SavedStates(scores, time, diff, Ps[0], Ps[1]);
            nList = doc.getElementsByTagName("Stack");
            for (int i = 0; i < nList.getLength(); i++) {
                final ArrayList<SaveShapeNode> left = new ArrayList<>();
                final ArrayList<SaveShapeNode> right = new ArrayList<>();
                final Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element eElement = (Element) nNode;
                    NodeList nList2 = eElement.getElementsByTagName("right");
                    for (int j = 0; j < nList2.getLength(); j++) {
                        final Node nNode2 = nList2.item(j);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            final Element eElement2 = (Element) nNode2;
                            final NodeList nList3 = eElement2.getElementsByTagName("element");
                            for (int k = 0; k < nList3.getLength(); k++) {
                                final Node nNode3 = nList3.item(k);
                                if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                                    final Element eElement3 = (Element) nNode3;
                                    final String className = eElement3.getElementsByTagName("name").item(0)
                                            .getTextContent();
                                    final int x = Integer
                                            .valueOf(eElement3.getElementsByTagName("x").item(0).getTextContent());
                                    final int y = Integer
                                            .valueOf(eElement3.getElementsByTagName("y").item(0).getTextContent());

                                    final Color color = colorMap
                                            .getColor(eElement3.getElementsByTagName("color").item(0).getTextContent()); // eElement2.getElementsByTagName("color").item(0).getTextContent();
                                    right.add(new SaveShapeNode(className, color, x, y));
                                }
                            }
                        }

                    }
                    nList2 = eElement.getElementsByTagName("left");
                    for (int j = 0; j < nList2.getLength(); j++) {
                        final Node nNode2 = nList2.item(j);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            final Element eElement2 = (Element) nNode2;
                            final NodeList nList3 = eElement2.getElementsByTagName("element");
                            for (int k = 0; k < nList3.getLength(); k++) {
                                final Node nNode3 = nList3.item(k);
                                if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                                    final Element eElement3 = (Element) nNode3;
                                    final String className = eElement3.getElementsByTagName("name").item(0)
                                            .getTextContent();
                                    final int x = Integer
                                            .valueOf(eElement3.getElementsByTagName("x").item(0).getTextContent());
                                    final int y = Integer
                                            .valueOf(eElement3.getElementsByTagName("y").item(0).getTextContent());

                                    final Color color = colorMap
                                            .getColor(eElement3.getElementsByTagName("color").item(0).getTextContent()); // eElement2.getElementsByTagName("color").item(0).getTextContent();
                                    left.add(new SaveShapeNode(className, color, x, y));
                                }
                            }
                        }

                    }

                    data[i] = new PlayersStacksData(right, left);
                }
            }
            load.buildGameState(g);
            load.buildPlayer(data);
            inputStream.close();
        } catch (final Exception e) {

        }

        return load;
    }

}
