/**
 *    Copyright 2017 ICF Olson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.citytechinc.cq.component.dialog.widgetcollection;

import org.codehaus.plexus.util.StringUtils;

import com.citytechinc.cq.component.dialog.DefaultDialogElementParameters;

public class WidgetCollectionParameters extends DefaultDialogElementParameters {
	private static final String PRIMARY_TYPE = "cq:WidgetCollection";
	private static final String DEFAULT_FIELD_NAME = "items";

	@Override
	public String getPrimaryType() {
		return PRIMARY_TYPE;
	}

	@Override
	public void setPrimaryType(String primaryType) {
		throw new UnsupportedOperationException("PrimaryType is Static for TagInputFieldWidget");
	}

	@Override
	public String getFieldName() {
		if (StringUtils.isEmpty(fieldName)) {
			return DEFAULT_FIELD_NAME;
		}
		return fieldName;
	}
}
