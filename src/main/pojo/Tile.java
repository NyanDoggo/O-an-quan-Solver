package main.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class Tile implements Serializable {

    public int a;

    @JsonIgnore
    public int tileIndex;
    @JsonIgnore
    public boolean isQuan;
    @JsonIgnore
    public Tile nextTile;
    @JsonIgnore
    public Tile previousTile;

    public Tile(boolean isQuan, int tileIndex) {
        this.tileIndex = tileIndex;
        if (isQuan) {
            this.initQuanTile();
        } else {
            this.initPeasantTile();
        }
    }

    public Tile(Tile tile) {
        this.a = tile.a;
        this.nextTile = SerializationUtils.clone(tile.nextTile);
        this.previousTile = SerializationUtils.clone(tile.previousTile);
        this.isQuan = tile.isQuan;
        this.tileIndex = tile.tileIndex;
    }

    public void initPeasantTile() {
        this.a = 5;
        this.isQuan = false;
    }

    public void initQuanTile() {
        this.a = 5;
        this.isQuan = true;
    }

    public void clearTile() {
        this.a = 0;
    }

    public int getTileIndex() {
        return this.tileIndex;
    }

    @Override
    public String toString() {
        if (this.isQuan) {
            return "Q" + a;
        } else {
            return Integer.toString(a);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (a != tile.a) return false;
        return isQuan == tile.isQuan;
    }

    @Override
    public int hashCode() {
        int result = a;
        result = 31 * result + (isQuan ? 1 : 0);
        return result;
    }
}
