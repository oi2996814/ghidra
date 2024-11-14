/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.plugin.core.calltree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import docking.widgets.tree.GTreeNode;
import generic.theme.GIcon;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Function;
import ghidra.program.util.FunctionSignatureFieldLocation;
import ghidra.program.util.ProgramLocation;
import ghidra.util.exception.CancelledException;
import ghidra.util.task.TaskMonitor;
import resources.MultiIcon;
import resources.icons.TranslateIcon;

public class ExternalCallNode extends CallNode {

	private static final Icon EXTERNAL_ICON = new GIcon("icon.plugin.calltree.node.external");
	private MultiIcon externalFunctionIcon;

	private final Function function;
	private final Address sourceAddress;
	private final String name;

	ExternalCallNode(Function function, Address sourceAddress,
			boolean isCallRef, CallTreeOptions callTreeOptions) {
		super(callTreeOptions);
		this.function = function;
		this.sourceAddress = sourceAddress;
		this.name = function.getName();
		this.isCallRef = isCallRef;

		externalFunctionIcon = new MultiIcon(EXTERNAL_ICON, false, 32, 16);
		TranslateIcon translateIcon =
			isCallRef ? new TranslateIcon(CallTreePlugin.FUNCTION_ICON, 16, 0)
					: new TranslateIcon(CallTreePlugin.DATA_ICON, 16, 0);
		externalFunctionIcon.addIcon(translateIcon);

	}

	@Override
	public int loadAll(TaskMonitor monitor) throws CancelledException {
		return 1; // this node cannot be opened
	}

	@Override
	CallNode recreate() {
		return new ExternalCallNode(function, sourceAddress, isCallRef, callTreeOptions);
	}

	@Override
	public Function getRemoteFunction() {
		return function;
	}

	@Override
	public ProgramLocation getLocation() {
		return new FunctionSignatureFieldLocation(function.getProgram(), function.getEntryPoint());
	}

	@Override
	public Address getSourceAddress() {
		return sourceAddress;
	}

	@Override
	public List<GTreeNode> generateChildren(TaskMonitor monitor) throws CancelledException {
		return new ArrayList<>();
	}

	@Override
	public Icon getIcon(boolean expanded) {
		return externalFunctionIcon;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getToolTip() {
		return "(External) " + super.getToolTip();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
