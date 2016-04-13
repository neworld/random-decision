package lt.neworld.randomdecision

/**
 * @author neworld
 * @since 09/04/16
 */

fun main(args: Array<String>) {
    if (args.any { it == "--no-gui" }) {
        CLI().run()
    } else {
        setupLookAndFeel()
        Window().run()
    }
}