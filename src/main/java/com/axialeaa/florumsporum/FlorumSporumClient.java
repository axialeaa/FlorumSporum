package com.axialeaa.florumsporum;

import net.fabricmc.api.ClientModInitializer;

//? <1.21.4
/*import com.axialeaa.florumsporum.data.model.SporeBlossomItemModelJson;*/

public class FlorumSporumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //? if <1.21.4
        /*SporeBlossomItemModelJson.registerModelPredicateProvider();*/
    }

}