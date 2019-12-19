package com.lch.lottery.search.response;

import com.lch.lottery.common.reponse.LotteryBaseResponse;

import java.util.List;

public class SearchPostResponse extends LotteryBaseResponse {

    public List<Item> items;

    public static class Item {

        public String postTitle;
        public String postId;

    }

}
