// Purpose: single source of truth for nav graph route names, so screens never hardcode strings.
// Author: Harrison Dsouza
package week11.st530550.finalproject.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val BROWSE = "browse"
    const val MY_POSTS = "myPosts"
    const val POST_LOST_ITEM = "postLostItem"
    const val EDIT_LOST_ITEM = "postLostItem/{itemId}"

    fun editLostItem(itemId: String) = "postLostItem/$itemId"
}
