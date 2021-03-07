package todo

import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import kotlinx.html.id
import react.RBuilder
import react.RClass
import styled.css
import styled.styledDiv
import tz.co.asoft.*

val scope = MainScope()
fun main() = document.getElementById("root").setContent {
    Grid {
        css {
            centerContent()
            height = 100.pct
            padding(horizontal = 2.em)
            onDesktop {
                gridTemplateColumns = GridTemplateColumns("1fr 1fr 1fr")
                gridTemplateRows = GridTemplateRows("1fr 1fr")
            }

            onMobile {
                gridTemplateColumns = GridTemplateColumns("1fr")
            }
        }

        Product(name = "Paper Plane", icon = FaPaperPlane, price = 200)
        Product(name = "Text Book", icon = FaBook, price = 500)
        Product(name = "Toy Mule", icon = FaStickerMule, price = 290)
        Product(name = "Toy Plane", icon = FaAvianex, price = 3500)
        Product(name = "Black Tie", icon = FaBlackTie, price = 99)
        Product(name = "Light Bulb", icon = FaMedapps, price = 450)
    }
}