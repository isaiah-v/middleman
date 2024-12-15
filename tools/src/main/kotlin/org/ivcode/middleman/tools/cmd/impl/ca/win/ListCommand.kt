package org.ivcode.middleman.tools.cmd.impl.ca.win

import org.ivcode.middleman.tools.certutil.CertutilService
import org.ivcode.middleman.tools.cmd.lib.Arguments
import org.ivcode.middleman.tools.cmd.lib.Command

class ListCommand: Command {
    override fun exec(args: Arguments) {
        CertutilService().store(
            user = true,
            certificateStoreName = "Root"
        )
    }

    override fun description(): String =
        "List certificates within a Certificate Authority"

}