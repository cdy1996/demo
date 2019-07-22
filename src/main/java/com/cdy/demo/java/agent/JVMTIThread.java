package com.cdy.demo.java.agent;


import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

/**
 * attach附着
 * Created by 陈东一
 * 2019/5/19 0019 19:25
 */
public class JVMTIThread {
    public static void main(String[] args)
            throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("AccountMain")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("D:\\hello\\out\\artifacts\\unnamed\\unnamed.jar ", "cxs");
                System.out.println("ok");
                virtualMachine.detach();
            }
        }
    }
}
