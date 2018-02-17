package com.eden.orchid.bsdoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.models.Social
import javax.inject.Inject

class BSDocTheme @Inject
constructor(context: OrchidContext) : Theme(context, "BsDoc", 100) {

    @Option @StringDefault("#4C376C")
    @Description("The CSS HEX value for the site's primary color.")
    lateinit var primaryColor: String

    @Option @StringDefault("#000000")
    @Description("The CSS HEX value for the site's secondary color.")
    lateinit var secondaryColor: String

    @Option
    @Description("Your social media links.")
    var social: Social? = null

    @Option
    @Description("Components to include in the sidebar, below the page menu.")
    lateinit var sidebar: ComponentHolder

    override fun loadAssets() {
        addCss("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css")
        addCss("https://cdnjs.cloudflare.com/ajax/libs/github-fork-ribbon-css/0.2.0/gh-fork-ribbon.min.css")
        addCss("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
        addCss("assets/css/bsdoc.scss")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js")
        addJs("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.3.0/bootbox.min.js")
        addJs("assets/js/bsdoc.js")
    }

    override fun getComponentHolders(): Array<ComponentHolder> {
        return arrayOf(sidebar)
    }
}
