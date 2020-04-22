package main.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.logic.State;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class Table {
    public HashMap<Integer, State> table = new HashMap<>();

    public void add(State state){
        this.table.put(state.hashCode(), state);
    }

    public HashMap<Integer, State> getTable() {
        return table;
    }

    //test this method
    public boolean exists(State state){
        return this.table.containsValue(state) || this.table.containsKey(state.hashCode());
    }

    public String serializeToJson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Table{" +
                "table=" + table +
                '}';
    }

//    public void saveFileJson(String filePath) throws IOException {
//        try{
//            Gson gson = new Gson();
//            Writer writer = new FileWriter(filePath);
//            gson.toJson(, writer);
//            writer.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @JsonIgnore
    public void saveToFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), this);
    }
}
