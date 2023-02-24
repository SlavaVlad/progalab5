package app

object CommandHandler {
    fun help() {
        println("""
            ${ConsoleColors.ANSI_CYAN}команда {аргумент} - инфо${ConsoleColors.ANSI_RESET}
            ${ConsoleColors.ANSI_BLUE}info${ConsoleColors.ANSI_RESET} - выводит информацию о коллекции
            ${ConsoleColors.ANSI_BLUE}show${ConsoleColors.ANSI_RESET} - вывод всех элементов коллекции в консоль
            ${ConsoleColors.ANSI_BLUE}add {element}${ConsoleColors.ANSI_RESET} - добавляет элемент в коллекцию
            ${ConsoleColors.ANSI_BLUE}update {id} {element}${ConsoleColors.ANSI_RESET} - обновляет значение элемента с указанным id
            ${ConsoleColors.ANSI_BLUE}remove_by_id {id}${ConsoleColors.ANSI_RESET} - удаляет элемент с указанным id
            ${ConsoleColors.ANSI_BLUE}clear${ConsoleColors.ANSI_RESET} - очищает коллекцию
            ${ConsoleColors.ANSI_BLUE}save${ConsoleColors.ANSI_RESET} - сохраняет коллекцию в файл
            ${ConsoleColors.ANSI_BLUE}execute_script {file_name}${ConsoleColors.ANSI_RESET} - выполняет последовательность команд из файла с указанным именем
            ${ConsoleColors.ANSI_BLUE}exit${ConsoleColors.ANSI_RESET} - заканчивает выполнение программы ${ConsoleColors.ANSI_RED}БЕЗ СОХРАНЕНИЯ${ConsoleColors.ANSI_RESET}
            ${ConsoleColors.ANSI_BLUE}add_if_max {element}${ConsoleColors.ANSI_RESET} - добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
            ${ConsoleColors.ANSI_BLUE}add_if_min {element}${ConsoleColors.ANSI_RESET} - добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
            ${ConsoleColors.ANSI_BLUE}remove_greater {element}${ConsoleColors.ANSI_RESET} - удаляет из коллекции все элементы, превышающие заданный
            ${ConsoleColors.ANSI_BLUE}group_counting_by_price${ConsoleColors.ANSI_RESET} - сгруппировать элементы коллекции по значению поля price, вывести количество элементов в каждой группе
            ${ConsoleColors.ANSI_BLUE}filter_starts_with_part_number partNumber${ConsoleColors.ANSI_RESET} - вывести элементы, значение поля partNumber которых начинается с заданной подстроки
            ${ConsoleColors.ANSI_BLUE}filter_greater_than_price price${ConsoleColors.ANSI_RESET} - вывести элементы, значение поля price которых больше заданного
        """.trimIndent())
    }
}