package main.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.SerializationUtils;

import java.beans.Transient;
import java.io.Serializable;

public class Tile implements Serializable {
    @Expose
    public int currentNumberOfStones;

    @Expose
    public int tileIndex;
    @Expose
    public boolean isQuan;
    public Tile nextTile;
    public Tile previousTile;

    public Tile() {

    }

    public Tile(boolean isQuan, int tileIndex) {
        this.tileIndex = tileIndex;
        if (isQuan) {
            this.initQuanTile();
        } else {
            this.initPeasantTile();
        }
    }

    public Tile(Tile tile) {
        this.currentNumberOfStones = tile.currentNumberOfStones;
        this.nextTile = SerializationUtils.clone(tile.nextTile);
        this.previousTile = SerializationUtils.clone(tile.previousTile);
        this.isQuan = tile.isQuan;
        this.tileIndex = tile.tileIndex;
    }

    public void initPeasantTile() {
        this.currentNumberOfStones = 5;
        this.isQuan = false;
    }

    public void initQuanTile() {
        this.currentNumberOfStones = 5;
        this.isQuan = true;
    }

    public void clearTile() {
        this.currentNumberOfStones = 0;
    }

    public int getTileIndex() {
        return this.tileIndex;
    }

    @Override
    public String toString() {
        if (this.isQuan) {
            return "Q" + currentNumberOfStones;
        } else {
            return Integer.toString(currentNumberOfStones);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (currentNumberOfStones != tile.currentNumberOfStones) return false;
        return isQuan == tile.isQuan;
    }

    @Override
    public int hashCode() {
        int result = currentNumberOfStones;
        result = 31 * result + (isQuan ? 1 : 0);
        return result;
    }
}
