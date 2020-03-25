package org.yudhistiraarya.graph;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;

public class SampleGraph {
    public static void main(String[] args) throws IOException {
        Node nasi = node("nasi").with(Color.RED);
        Node lemak = node("lemak");
        Node biasa = node("biasa");
        Node ayam = node("ayam");
        Node sotong = node("sotong");

        Graph g = graph("example1").directed()
                .graphAttr().with(RankDir.LEFT_TO_RIGHT)
                .with(
                        nasi.link(lemak),
                        lemak.link(
                                to(sotong).with(Style.DASHED),
                                to(biasa),
                                to(ayam))
                );

        // output to image
        Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File("banana.png"));

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // output to byte stream
        Graphviz.fromGraph(g).width(700).render(Format.SVG).toOutputStream(byteArrayOutputStream);

        final Base64.Encoder encoder = Base64.getEncoder();

        final StringBuilder html = new StringBuilder("<!DOCTYPE html><html><head><title>Nasi Lemak!</title>");
        html.append("<body>");
        html.append("<img src=\"data:image/svg+xml;base64,");
        html.append(encoder.encodeToString(byteArrayOutputStream.toByteArray()));
        html.append("\"/>");
        html.append("</body></html>");

        Files.write(Paths.get("nasilemak.html"), html.toString().getBytes(StandardCharsets.UTF_8));
    }
}
