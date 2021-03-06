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
package com.citytechinc.cq.component.touchuidialog.widget.multifield;

import com.citytechinc.cq.component.annotations.config.TouchUIWidget;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.touchuidialog.widget.AbstractTouchUIWidget;
import com.citytechinc.cq.component.touchuidialog.widget.DefaultTouchUIWidgetParameters;

@TouchUIWidget(annotationClass = MultiField.class, makerClass = MultiFieldWidgetMaker.class,
	resourceType = MultiFieldWidget.RESOURCE_TYPE, ranking = MultiFieldWidget.RANKING)
public class MultiFieldWidget extends AbstractTouchUIWidget {

	public static final String RESOURCE_TYPE = "granite/ui/components/foundation/form/multifield";
	public static final int RANKING = 1000000;

	public MultiFieldWidget(DefaultTouchUIWidgetParameters parameters) {
		super(parameters);
	}

}
