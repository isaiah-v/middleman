package org.ivcode.middleman.tools.cmd.lib

class Arguments (
    val args: List<String>,
    val index: Int = 0
) {
    constructor(args: Array<String>, index: Int = 0): this(args.asList(), index)

    /**
     * Returns the context. The context is the arguments made before the current index.
     */
    fun context(): List<String> {
        return args.subList(0, index)
    }

    /**
     * Returns the arguments. The arguments are the arguments made after the current index.
     */
    fun arguments(): List<String> {
        if(index>args.size) {
            return emptyList()
        }
        return args.subList(index, args.size)
    }

    /**
     * Returns an argument at the specified index. The default index is the current index.
     */
    fun get(index: Int = this.index): String? {
        if(index>=args.size) {
            return null
        }
        return args[index]
    }

    /**
     * Returns the next argument. The default index is the current index.
     */
    fun next(index: Int = this.index+1): Arguments {
        if (index > args.size) {
            throw IndexOutOfBoundsException()
        }
        return Arguments(args, index)
    }

}