package com.tmiyamon.mdicons

import com.google.gson.Gson

class MaterialColorLoader {
    static final FILE_NAME = "com/tmiyamon/mdicons/colors.json"

    static Map<String, String> load() {
        def json = MaterialColorLoader.class.getClassLoader().getResource(FILE_NAME).text
        new Gson().fromJson(json, new HashMap<String, String>().getClass()) as Map<String, String>
    }
}
