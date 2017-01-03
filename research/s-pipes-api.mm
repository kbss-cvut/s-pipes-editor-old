<map version="freeplane 1.5.9">
<!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
<node TEXT="s-pipes api" FOLDED="false" ID="ID_448131109" CREATED="1312805290594" MODIFIED="1481792167810">
<font NAME="SansSerif"/>
<hook NAME="MapStyle" zoom="1.614">
    <properties show_icon_for_attributes="true" fit_to_viewport="false;" show_note_icons="true" show_notes_in_map="true"/>

<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node" STYLE="oval" UNIFORM_SHAPE="true" VGAP_QUANTITY="24.0 pt">
<font SIZE="24"/>
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="default" MAX_WIDTH="600.0 px" COLOR="#000000" STYLE="as_parent">
<font NAME="SansSerif" SIZE="10" BOLD="false" ITALIC="false"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.attributes">
<font SIZE="9"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.note"/>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<edge STYLE="hide_edge"/>
<cloud COLOR="#f0f0f0" SHAPE="ROUND_RECT"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900">
<font NAME="Liberation Sans" SIZE="10" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.important">
<icon BUILTIN="yes"/>
</stylenode>
<stylenode TEXT="onto-prop" COLOR="#990000"/>
<stylenode TEXT="categorization" COLOR="#009933"/>
<stylenode TEXT="comment" COLOR="#999999">
<font SIZE="8"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right" STYLE="bubble">
<stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000">
<font SIZE="18"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff">
<font SIZE="16"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439">
<font SIZE="14"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000">
<font SIZE="12"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111">
<font SIZE="10"/>
</stylenode>
</stylenode>
</stylenode>
</map_styles>
</hook>
<hook NAME="accessories/plugins/CreationModificationPlugin_new.properties"/>
<node TEXT="scenario" STYLE_REF="categorization" POSITION="right" ID="ID_743457031" CREATED="1481803309926" MODIFIED="1481803317652">
<node TEXT="create new script" ID="ID_343157323" CREATED="1481803386227" MODIFIED="1481803392413">
<node TEXT="part" STYLE_REF="onto-prop" ID="ID_1539678270" CREATED="1481803395022" MODIFIED="1481803396728">
<node TEXT="specify new context" ID="ID_167531040" CREATED="1481803439266" MODIFIED="1481803579835"/>
<node TEXT="assign default module-types-ontology (MTO) uri" ID="ID_1390358112" CREATED="1481803583688" MODIFIED="1481803660241">
<node TEXT="e.g." STYLE_REF="onto-prop" ID="ID_116140066" CREATED="1481803792040" MODIFIED="1481803793251">
<node ID="ID_222554479" CREATED="1481803794189" MODIFIED="1481803794189" LINK="http://onto.fel.cvut.cz/ontologies/lib/module&amp;gt;"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <span style="color: #000000; background-color: #ffffff"><font color="#000000" face="monospace">&lt;http://onto.fel.cvut.cz/ontologies/lib/module&gt;</font></span><span style="font-family: monospace"><font face="monospace"><br/></font></span>
  </body>
</html>

</richcontent>
</node>
</node>
</node>
<node TEXT="load module types based on MTO uri" ID="ID_193006776" CREATED="1481803318705" MODIFIED="1481803653655">
<node TEXT="calls" STYLE_REF="onto-prop" ID="ID_237710529" CREATED="1481803702758" MODIFIED="1481803704340">
<node TEXT="s-pipes service" ID="ID_1206527077" CREATED="1481803683252" MODIFIED="1481803705963"/>
</node>
</node>
</node>
</node>
<node TEXT="modify and save script" ID="ID_1126639632" CREATED="1481803804931" MODIFIED="1481803845970">
<node TEXT="pre-condition" STYLE_REF="onto-prop" ID="ID_1062502456" CREATED="1481804102790" MODIFIED="1481804112056">
<node TEXT="script is loaded in s-pipes" ID="ID_179558890" CREATED="1481804112895" MODIFIED="1481804183386"/>
<node TEXT="script context = $S_CTX" ID="ID_1244857914" CREATED="1481804138282" MODIFIED="1481804162003"/>
</node>
<node TEXT="part" STYLE_REF="onto-prop" ID="ID_1280253558" CREATED="1481803854134" MODIFIED="1481803856046">
<node TEXT="add module" ID="ID_762149496" CREATED="1481803857065" MODIFIED="1481803928252">
<node TEXT="drag node-type to canvas" ID="ID_226898526" CREATED="1481806569627" MODIFIED="1481806605590">
<node TEXT="executes" STYLE_REF="onto-prop" ID="ID_63679085" CREATED="1481804441335" MODIFIED="1481806655209">
<node TEXT="show simple label prompt" ID="ID_1832942519" CREATED="1481805624910" MODIFIED="1481805717923">
<node TEXT="label" ID="ID_62472286" CREATED="1481805725267" MODIFIED="1481805726716"/>
</node>
<node TEXT="POST" STYLE_REF="categorization" ID="ID_376212910" CREATED="1481804673059" MODIFIED="1481804682450">
<node TEXT="sped/graphs/$S_CTX/nodes" ID="ID_720594923" CREATED="1481804683400" MODIFIED="1481804744998">
<node TEXT="calls" STYLE_REF="onto-prop" ID="ID_1098229017" CREATED="1481804751028" MODIFIED="1481804752759">
<node TEXT="retrieve module-type form" ID="ID_1078987498" CREATED="1481804478339" MODIFIED="1481805394460">
<node TEXT="GET" STYLE_REF="categorization" ID="ID_438675338" CREATED="1481804267363" MODIFIED="1481804269706">
<node TEXT="s-pipes/$S_CTX/module-types/1234/form" ID="ID_1464202394" CREATED="1481804300375" MODIFIED="1481805371472">
<node TEXT="returns" STYLE_REF="onto-prop" ID="ID_1101754617" CREATED="1481804283681" MODIFIED="1481804285480">
<node TEXT="{&#xa;    &quot;@id&quot;: form:deploy_q ,&#xa;    &quot;@type&quot;: doc:question,&#xa;    &quot;layout-class&quot;: &quot;form&quot;&#xa;    &quot;label: &quot;Deploy to sesame&quot;&#xa;    &quot;has-related-question&quot;: [&#xa;          { &quot;@id&quot;: form:repository-url-q }&#xa;    ]&#xa;}" ID="ID_1728068254" CREATED="1481793735766" MODIFIED="1481803206605"/>
</node>
</node>
</node>
</node>
</node>
<node TEXT="returns" STYLE_REF="onto-prop" ID="ID_1830935565" CREATED="1481804758030" MODIFIED="1481804759938">
<node TEXT="JSON-LD" ID="ID_677743030" CREATED="1481804760287" MODIFIED="1481804767826"/>
<node TEXT="Location:" ID="ID_1560929799" CREATED="1481806504990" MODIFIED="1481806515309">
<node TEXT="sped/graphs/$S_CTX/nodes/15555" ID="ID_1420688902" CREATED="1481806515986" MODIFIED="1481806531642"/>
</node>
</node>
<node TEXT="params" STYLE_REF="onto-prop" ID="ID_800234533" CREATED="1481805027018" MODIFIED="1481805029939">
<node TEXT="X" ID="ID_1980308962" CREATED="1481805031494" MODIFIED="1481805053817"/>
<node TEXT="Y" ID="ID_1272730150" CREATED="1481805043521" MODIFIED="1481805056643"/>
</node>
</node>
</node>
<node TEXT="show module form to user" ID="ID_563760959" CREATED="1481804395312" MODIFIED="1481804846764">
<node TEXT="part" STYLE_REF="onto-prop" ID="ID_516501340" CREATED="1481806339892" MODIFIED="1481806344326">
<node TEXT="set rdfs:label correctly (should be part of semforms API)" ID="ID_432029004" CREATED="1481806344827" MODIFIED="1481806359574"/>
<node TEXT="uri is generated automatically based on rdfs:label" ID="ID_494879115" CREATED="1481806470559" MODIFIED="1481806487875"/>
</node>
</node>
</node>
</node>
<node TEXT="click module form save button" ID="ID_1026856538" CREATED="1481806606985" MODIFIED="1481806690221">
<node TEXT="executes" STYLE_REF="onto-prop" ID="ID_1630514168" CREATED="1481806693322" MODIFIED="1481806696120">
<node TEXT="create module" ID="ID_306186520" CREATED="1481804046423" MODIFIED="1481804476444">
<node TEXT="POST" STYLE_REF="categorization" ID="ID_1534318033" CREATED="1481804201230" MODIFIED="1481804202801">
<node TEXT="s-pipes/$S_CTX/modules/form" ID="ID_383697174" CREATED="1481804052850" MODIFIED="1481807289352">
<node TEXT="returns" STYLE_REF="onto-prop" ID="ID_174914025" CREATED="1481804283681" MODIFIED="1481804285480">
<node TEXT="s-pipes/$S_CTX/modules/1234" ID="ID_144381327" CREATED="1481804300375" MODIFIED="1481804305243"/>
</node>
</node>
</node>
</node>
</node>
</node>
</node>
<node TEXT="add edge" ID="ID_1700052407" CREATED="1481803876481" MODIFIED="1481803880180"/>
<node TEXT="delete node" ID="ID_1833437172" CREATED="1481803889150" MODIFIED="1481803892086"/>
<node TEXT="delete edge" ID="ID_453545579" CREATED="1481803892253" MODIFIED="1481803894776"/>
<node TEXT="save" ID="ID_1737068883" CREATED="1481803884479" MODIFIED="1481803885901"/>
</node>
</node>
<node TEXT="load script" ID="ID_127044795" CREATED="1481803849102" MODIFIED="1481803852304"/>
</node>
<node TEXT="executions" STYLE_REF="categorization" POSITION="right" ID="ID_150293184" CREATED="1481792168814" MODIFIED="1481794171139">
<node TEXT="service?id=$FUNCTION_ID&amp;param1=...&amp;" ID="ID_1048851159" CREATED="1481792807965" MODIFIED="1481792959054">
<node TEXT="desc" STYLE_REF="onto-prop" ID="ID_217031794" CREATED="1481792879102" MODIFIED="1481792879894">
<node TEXT="$FUNCTION_ID is URI or its localName" ID="ID_1522490842" CREATED="1481792880507" MODIFIED="1481792980953"/>
</node>
</node>
<node TEXT="module?id=$MODULE_ID&amp;context=$CONTEXT&amp;param1= ...&amp;" ID="ID_212098483" CREATED="1481792856728" MODIFIED="1481793006622">
<node TEXT="desc" STYLE_REF="onto-prop" ID="ID_1293852888" CREATED="1481792879102" MODIFIED="1481792879894">
<node TEXT="$MODULE_ID is URI or its localName" ID="ID_959066169" CREATED="1481792880507" MODIFIED="1481792976649"/>
</node>
</node>
</node>
<node TEXT="definitions" STYLE_REF="categorization" POSITION="right" ID="ID_408481384" CREATED="1481792189310" MODIFIED="1481794164167">
<node TEXT="/scripts" ID="ID_1332513590" CREATED="1481792228205" MODIFIED="1481792255891">
<node TEXT="/$id" ID="ID_527701113" CREATED="1481793062343" MODIFIED="1481793069413">
<node TEXT="/modules" ID="ID_548469027" CREATED="1481793085512" MODIFIED="1481793090033">
<node TEXT="/$id" ID="ID_261897456" CREATED="1481803059989" MODIFIED="1481803062336">
<node TEXT="form" ID="ID_779227103" CREATED="1481803064394" MODIFIED="1481803068786">
<node TEXT="returns" STYLE_REF="onto-prop" ID="ID_1080241736" CREATED="1481793732148" MODIFIED="1481793734207">
<node TEXT="{&#xa;    &quot;@id&quot;: form:deploy_q ,&#xa;    &quot;@type&quot;: doc:question,&#xa;    &quot;layout-class&quot;: &quot;form&quot;&#xa;    &quot;label: &quot;Deploy to sesame&quot;&#xa;    &quot;has-related-question&quot;: [&#xa;          { &quot;@id&quot;: form:repository-url-q }&#xa;    ]&#xa;}" ID="ID_450076707" CREATED="1481793735766" MODIFIED="1481803206605"/>
</node>
</node>
</node>
</node>
<node TEXT="/data" ID="ID_1653425295" CREATED="1481793097955" MODIFIED="1481793109243">
<node ID="ID_1029304909" CREATED="1481793166251" MODIFIED="1481793572942"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      ?closure=[<font color="#008000">true</font>/false]
    </p>
  </body>
</html>

</richcontent>
</node>
<node ID="ID_261549381" CREATED="1481793144467" MODIFIED="1481793575494"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      ?inferred=[true/<font color="#008000">false</font>]
    </p>
  </body>
</html>

</richcontent>
</node>
</node>
</node>
</node>
<node TEXT="/functions" ID="ID_1890776953" CREATED="1481793631572" MODIFIED="1481793636635">
<node TEXT="/$id" ID="ID_1948629367" CREATED="1481793062343" MODIFIED="1481793069413"/>
</node>
<node TEXT="/modules" ID="ID_1101918109" CREATED="1481803267793" MODIFIED="1481803272128"/>
<node TEXT="/module-types" ID="ID_156215290" CREATED="1481803272268" MODIFIED="1481803277844"/>
<node TEXT="/contexts" ID="ID_1262511456" CREATED="1481792354975" MODIFIED="1481792357461">
<node TEXT="/$id" ID="ID_465279590" CREATED="1481793481190" MODIFIED="1481793493853">
<node ID="ID_473266973" TREE_ID="ID_548469027">
<node ID="ID_226457313" TREE_ID="ID_261897456">
<node ID="ID_1996617442" TREE_ID="ID_779227103">
<node ID="ID_1139349328" TREE_ID="ID_1080241736">
<node ID="ID_525650978" TREE_ID="ID_450076707"/>
</node>
</node>
</node>
</node>
<node TEXT="/module-types" ID="ID_1191896999" CREATED="1481793640538" MODIFIED="1481793645331">
<node TEXT="/$id" ID="ID_1403816702" CREATED="1481793062343" MODIFIED="1481793069413">
<node TEXT="returns" STYLE_REF="onto-prop" ID="ID_703629658" CREATED="1481793732148" MODIFIED="1481793734207">
<node TEXT="{&#xa;    &quot;@id&quot;: kbss-module:deploy ,&#xa;    &quot;@type&quot;: sm:Module,&#xa;    &quot;label: &quot;Deploy to sesame&quot;&#xa;}" ID="ID_504690946" CREATED="1481793735766" MODIFIED="1481793953971"/>
</node>
</node>
</node>
<node ID="ID_1859042692" TREE_ID="ID_1653425295">
<node ID="ID_858545342" TREE_ID="ID_1029304909"/>
<node ID="ID_1604922199" TREE_ID="ID_261549381"/>
</node>
</node>
</node>
</node>
<node TEXT="validation" STYLE_REF="categorization" POSITION="right" ID="ID_1843914250" CREATED="1481803508269" MODIFIED="1481803511933"/>
</node>
</map>