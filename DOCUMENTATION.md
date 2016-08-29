# zaibalo-api documentation

**Get posts service**
----
  <_Additional information about your API call. Try to use verbs that match both request type (fetching vs modifying) and plurality (one vs multiple)._>

* **URL**

  /posts

* **Method:**

  `GET`
  
*  **URL Params**

   <_If URL params exist, specify them in accordance with name mentioned in URL section. Separate into optional and required. Document data constraints._> 

   **Required:**
 
   `id=[integer]`

   **Optional:**
 
   `photo_id=[alphanumeric]`

* **Data Params**

  <_If making a post request, what should the body payload look like? URL Params rules apply here too._>

* **Success Response:**
  
  <_What should the status code be on success and is there any returned data? This is useful when people need to to know what their callbacks should expect!_>

  * **Code:** 200 <br />
    **Content:** 
```json
    {
   "_links":{
      "addPost":{
         "href":"/posts"
      }
   },
   "_embedded":{
      "posts":[
         {
            "id":271,
            "content":"sfdsdf",
            "author":{
               "id":1,
               "displayName":"Dude",
               "photo":"https://scontent.xx.fbcdn.net/hprofile-xlt1/v/t1.0-1/c0.0.50.50/p50x50/11873380_104213273266939_5057856206594145694_n.jpg?oh=da0f7cf535d191e103b7774cc05f13fc&oe=57BACF87",
               "loginName":"207686369586295",
               "photoProvider":"FACEBOOK"
            },
            "creationTimestamp":1472484644000,
            "attachments":[],
            "ratings":[],
            "ratingSum":0,
            "ratingCount":0,
            "_links":{
               "addComment":{
                  "href":"/posts/271/comments"
               },
               "delete":{
                  "href":"/posts/271"
               },
               "self":{
                  "href":"/posts/271"
               }
            }
         }
      ]
   }
}
```
 
* **Error Response:**

  <_Most endpoints will have many ways they can fail. From unauthorized access, to wrongful parameters etc. All of those should be liste d here. It might seem repetitive, but it helps prevent assumptions from being made where they should be._>

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "Log in" }`

  OR

  * **Code:** 422 UNPROCESSABLE ENTRY <br />
    **Content:** `{ error : "Email Invalid" }`

* **Sample Call:**

  <_Just a sample call to your endpoint in a runnable format ($.ajax call or a curl request) - this makes life easier and more predictable._> 

* **Notes:**

  <_This is where all uncertainties, commentary, discussion etc. can go. I recommend timestamping and identifying oneself when leaving comments here._> 
