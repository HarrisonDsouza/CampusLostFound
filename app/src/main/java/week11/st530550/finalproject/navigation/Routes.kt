package week11.st530550.finalproject.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val BROWSE = "browse"
    const val MY_POSTS = "myPosts"
    const val PROFILE = "profile"
    const val POST_ITEM = "postItem/{kind}"
    const val EDIT_ITEM = "editItem/{itemId}"
    const val MATCH_REVIEW = "matchReview/{lostItemId}/{foundItemId}"

    fun postItem(kind: String) = "postItem/$kind"
    fun editItem(itemId: String) = "editItem/$itemId"
    fun matchReview(lostItemId: String, foundItemId: String) = "matchReview/$lostItemId/$foundItemId"
}
