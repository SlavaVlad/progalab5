package persistence.console

import java.util.regex.Pattern

enum class CPT(val pattern: Pattern) {
     BOOL(Pattern.compile("[true|false]")),
     STRING(Pattern.compile(".*")),
     INTEGER(Pattern.compile("[+-]?[0-9]*")),
     DOUBLE(Pattern.compile("[+-]?([0-9]*[.])?[0-9]+")),
     FILEPATH(Pattern.compile("^([a-zA-Z]:)?(\\\\[^<>:\"/\\\\|?*]+)+\\\\?\n")),
     JSON(Pattern.compile("""^\s*(\{.*\}|\[.*\])\s*$"""))
}
