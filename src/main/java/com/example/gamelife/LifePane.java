package com.example.gamelife;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

/**
 * Tabla na riadenie jednotlivých častí hry.
 */
public class LifePane extends Pane {
    private static Set<Integer> rulesSet = new HashSet<>(Set.of(0, 1, 2, 3));
    private final Font textFont = new Font("Cambria", 16);
    private int section = 0;
    private double currentSpeed = 0.5;
    private String currentSize = "40x40";
    private Rectangle[][] mapRec = new Rectangle[40][40];
    private boolean paused = false;
    private boolean initialized = false;

    LifePane() {
        widthProperty().addListener(e -> {
            if (section == 0) settings();
            if (section == 1) pick();
            if (section == 2) paint();
        });
        heightProperty().addListener(e -> {
            if (section == 0) settings();
            if (section == 1) pick();
            if (section == 2) paint();
        });
    }

    /**
     * Metóda vráti množinu aktuálne používaných pravidiel.
     * @return Množina aktuálnych pravidiel.
     */
    public static Set<Integer> getRules() {
        return rulesSet;
    }

    /**
     * Metóda vracia aktuálnu fázu hry.
     * @return Aktuálna fáza.
     */
    public int getSection() {
        return section;
    }

    /**
     * Metóda resetuje základné nastavenia hry.
     */
    private void resetProperties() {
        initialized = false;
        currentSpeed = 0.5;
        currentSize = "40x40";
        mapRec = new Rectangle[40][40];
        rulesSet = new HashSet<>(Set.of(0, 1, 2, 3));
        paused = false;
    }

    /**
     * Metóda prepne stav bunky a podľa neho aktualizuje jej farbu.
     * @param row Index riadku bunky.
     * @param col Index stĺpca bunky.
     */
    private void pickedCell(int row, int col) {
        if (section != 1) return;
        Cell c = GameLife.map[row][col];
        c.switchLive();
        mapRec[row][col].setFill(c.alive() ? Color.BLACK : Color.WHITE);
    }

    /**
     * Metóda vykresľuje možnosti nastavenie hry.
     *<p>
     * Umožňuje nastaviť rýchlosť simulácie, výber
     * z možnosťí rozmerov vykresľovanej mriežky
     * a výber pravidiel použitých na simuláciu.
     */
    protected void settings() {
        resetProperties();

        section = 0; // Settings
        getChildren().clear();

        double width = getWidth();
        double height = getHeight();

        // Background
        Rectangle bg = new Rectangle(0, 0, width, height);
        bg.setFill(Color.CORNFLOWERBLUE);

        Rectangle bg2 = new Rectangle(width / 30, height / 50, width * 28 / 30, height * 48 / 50);
        bg2.setFill(Color.BEIGE);

        // Title
        Text t = new Text("GAME LIFE");
        t.setFill(Color.BLACK);
        t.setFont(new Font("Cambria", 24));
        t.setX(width / 2 - t.getLayoutBounds().getWidth() / 2);
        t.setY(height / 15);

        // Slider
        Text sliderLabel = new Text("Simulation Speed (s)");
        sliderLabel.setFont(textFont);
        sliderLabel.setX(width / 20);
        sliderLabel.setY(3 * height / 20);

        Slider slider = new Slider(0.1, 1.5, currentSpeed);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.5);
        slider.setBlockIncrement(0.1);
        slider.setLayoutX(width / 20);
        slider.setLayoutY(3 * height / 15);
        slider.valueProperty().addListener(e -> {
            currentSpeed = slider.getValue();
            GameLife.setSpeed(currentSpeed * 1000);
        });

        // Map size
        Text boxLabel = new Text("Map Size");
        boxLabel.setFont(textFont);
        boxLabel.setX(width / 20);
        boxLabel.setY(6 * height / 20);

        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("20x20", "30x30", "40x40", "60x60");
        box.setPromptText("Size");
        box.setValue(currentSize);
        box.setLayoutX(width / 20);
        box.setLayoutY(5 * height / 15);
        box.setOnAction(e -> {
            currentSize = box.getValue();
        });

        Text cbLabel = new Text("Rules");
        cbLabel.setFont(textFont);
        cbLabel.setX(width / 20);
        cbLabel.setY(9 * height / 20);

        getChildren().addAll(bg, bg2, t, sliderLabel, slider, boxLabel, box, cbLabel);

        String[] rules = {"Underpopulation", "Survival", "Overpopulation", "Reproduction"};
        int y = 10;
        for (int i = 0; i < rules.length; i++) {
            CheckBox checkbox = new CheckBox(rules[i]);
            checkbox.setLayoutX(width / 20);
            checkbox.setLayoutY(y * height / 20);
            checkbox.setSelected(true);
            int finalI = i;
            checkbox.setOnAction(e -> {
                if (checkbox.isSelected()) {
                    rulesSet.add(finalI);
                } else {
                    rulesSet.remove(finalI);
                }
            });
            y++;
            getChildren().add(checkbox);
        }

        // Next
        Button next = new Button("Next");
        next.setLayoutX(width - width / 9);
        next.setLayoutY(height - height / 20);
        next.setOnAction(e -> pick());

        getChildren().addAll(next);
    }

    /**
     * Metóda vykresľuje fázu nastavovania herného plánu.
     *
     * Inicializuje pole buniek, pokiaľ to už nebolo urobené.
     * Následne pri každom zavolaní, metóda vykreslí bunky.
     */
    protected void pick() {
        if (!initialized) {
            String[] d = currentSize.split("x");
            int rows = Integer.parseInt(d[0]);
            int cols = Integer.parseInt(d[1]);
            GameLife.map = new Cell[rows][cols];
            mapRec = new Rectangle[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    GameLife.map[i][j] = new Cell();
                    mapRec[i][j] = new Rectangle();
                }
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    addNeighbours(i, j);
                }
            }
        }

        initialized = true;

        section = 1; // Pick
        getChildren().clear();

        double width = getWidth();
        double height = getHeight();

        draw();

        Button next = new Button("Next");
        next.setLayoutX(width - width / 9);
        next.setLayoutY(height - height / 20);
        next.setOnAction(e -> paint());

        getChildren().add(next);
    }

    /**
     * Metóda spúšťa simuláciu hry.
     *
     * V každom kroku behu simulácie sú prepočítavané rozmery buniek
     * podľa aktuálnych rozmerov plátna.
     * Pokiaľ nie je simulácia pozastavená, počíta sa stav buniek
     * v novej generácii.
     */
    protected void paint() {
        section = 2; // Paint
        getChildren().clear();

        var height = getHeight() * ((double) 19 / 20);
        double cellWidth = getWidth() / GameLife.map[0].length;
        double cellHeight = height / GameLife.map.length;

        for (int i = 0; i < GameLife.map.length; i++) {
            for (int j = 0; j < GameLife.map[0].length; j++) {
                Cell c = GameLife.map[i][j];
                c.update();
                var rec = mapRec[i][j];
                rec.setFill(c.alive() ? Color.BLACK : Color.WHITE); // Update the cell's color
                rec.setX(j * cellWidth);
                rec.setY(i * cellHeight);
                rec.setWidth(cellWidth);
                rec.setHeight(cellHeight);
                getChildren().add(rec);
            }
        }

        if (!paused) {
            for (Cell[] row : GameLife.map) {
                for (Cell c : row) {
                    c.newGen();
                }
            }
        }

        Button stop = new Button(!paused ? "Pause" : "Resume");
        stop.setLayoutX(getWidth() / 9);
        stop.setLayoutY(getHeight() * 19 / 20);
        stop.setOnAction(e -> {
            if (stop.getText().equals("Pause")) {
                paused = true;
            } else if (stop.getText().equals("Resume")) {
                paused = false;
            }
        });

        Button retry = new Button("Retry");
        retry.setLayoutX(getWidth() * 2 / 9);
        retry.setLayoutY(getHeight() * 19 / 20);
        retry.setOnAction(e -> settings());

        getChildren().addAll(stop, retry);
    }

    /**
     * Metóda vykreslí incializovanú mapu buniek.
     *
     * Rozmery buniek sú vypočítané podľa rozmerov plátna,
     * na ktorom sú vykreslené.
     */
    protected void draw() {
        getChildren().clear();
        var height = getHeight() * ((double) 19 / 20);
        double cellWidth = getWidth() / GameLife.map[0].length;
        double cellHeight = height / GameLife.map.length;

        for (int row = 0; row < GameLife.map.length; row++) {
            for (int col = 0; col < GameLife.map[row].length; col++) {
                Cell cell = GameLife.map[row][col];
                Rectangle r = new Rectangle(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                r.setFill(cell.alive() ? Color.BLACK : Color.WHITE);
                r.setStroke(Color.LIGHTGRAY);
                int x = row;
                int y = col;
                r.setOnMouseClicked(e -> pickedCell(x, y));

                mapRec[row][col] = r;
                getChildren().add(r);
            }
        }
    }

    /**
     * Metóda pridá konkrétnej bunke všetky jej susedné bunky.
     * @param row Index riadku bunky.
     * @param col Index stĺpca bunky.
     */
    protected void addNeighbours(int row, int col) {
        Cell cell = GameLife.map[row][col];
        int[] dim = {-1, 0, 1};
        for (int r : dim) {
            for (int c : dim) {
                if (r == 0 && c == 0) continue;
                if (row + r < 0 || row + r >= GameLife.map.length) continue;
                if (col + c < 0 || col + c >= GameLife.map[0].length) continue;
                cell.addNeighbour(GameLife.map[row + r][col + c]);
            }
        }
    }
}