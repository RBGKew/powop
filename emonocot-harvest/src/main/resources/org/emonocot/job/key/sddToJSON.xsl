<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://rs.tdwg.org/UBIF/2006/" xmlns:sdd="http://rs.tdwg.org/UBIF/2006/" xsi:schemaLocation="http://rs.tdwg.org/UBIF/2006/ http://www.lucidcentral.org/2006/SDD/SDD1.1/SDD.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:exsl="http://exslt.org/common"
  xmlns:str="http://exslt.org/strings"
  xmlns:math="http://exslt.org/math"
  extension-element-prefixes="exsl str">
  <xsl:output method="text" version="1.0" omit-xml-declaration="yes" indent="no" />
  <xsl:param name="imagePath"/>
  <xsl:param name="linkPath"/>
  <xsl:param name="taxonFileName"/>
  <xsl:param name="imageFileName"/>
  
  <xsl:variable name="taxon-map">
    <sdd:TaxonNames>
      <xsl:for-each select="document($taxonFileName)/sdd:TaxonNames/sdd:TaxonName">
        <sdd:TaxonName>
          <xsl:attribute name="id">
            <xsl:value-of select="@id"/>
          </xsl:attribute>
          <xsl:attribute name="debuglabel">
            <xsl:value-of select="@debuglabel"/>
          </xsl:attribute>
        </sdd:TaxonName>
      </xsl:for-each>
    </sdd:TaxonNames>  
  </xsl:variable>
 
 <xsl:variable name="image-map">
    <sdd:MediaObjects>
      <xsl:for-each select="document($imageFileName)/sdd:MediaObjects/sdd:MediaObject">
        <sdd:MediaObject>
          <xsl:attribute name="id">
            <xsl:value-of select="@id"/>
          </xsl:attribute>
          <xsl:attribute name="debuglabel">
            <xsl:value-of select="@debuglabel"/>
          </xsl:attribute>
        </sdd:MediaObject>
      </xsl:for-each>
    </sdd:MediaObjects>  
  </xsl:variable>
  
  <xsl:variable name="lead-indexes">
    <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:IdentificationKeys/sdd:IdentificationKey/sdd:Leads/sdd:Lead">
      <sdd:Lead>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>        
        <xsl:attribute name="position">
          <xsl:value-of select="position()"/>
        </xsl:attribute>
      </sdd:Lead>
    </xsl:for-each>
 </xsl:variable>
  
  <xsl:variable name="character-indexes">
    <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:Characters/*">
      <sdd:Character>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="name">
          <xsl:value-of select="sdd:Representation/sdd:Label"/>
        </xsl:attribute>
        <xsl:attribute name="position">
          <xsl:value-of select="position() - 1"/>
        </xsl:attribute>
      </sdd:Character>
    </xsl:for-each>
 </xsl:variable>

 <xsl:variable name="descriptiveConcept-indexes">
    <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:DescriptiveConcepts/sdd:DescriptiveConcept">
      <sdd:DescriptiveConcept>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="name">
          <xsl:value-of select="sdd:Representation/sdd:Label"/>
        </xsl:attribute>
        <xsl:attribute name="position">
          <xsl:value-of select="position() - 1"/>
        </xsl:attribute>
      </sdd:DescriptiveConcept>
    </xsl:for-each>
 </xsl:variable>

 <xsl:variable name="taxon-indexes">
    <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:TaxonNames/sdd:TaxonName">
      <sdd:TaxonName>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="name">
          <xsl:value-of select="sdd:Representation/sdd:Label"/>
        </xsl:attribute>
        <xsl:attribute name="position">
          <xsl:value-of select="position() - 1"/>
        </xsl:attribute>
      </sdd:TaxonName>
    </xsl:for-each>
 </xsl:variable>


  <xsl:variable name="state-indexes">
    <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:Characters/sdd:CategoricalCharacter">
      <xsl:variable name="char" select="@id"/>
	  <xsl:variable name="character" select="exsl:node-set($character-indexes)/sdd:Character[@id = $char]"/>
      <xsl:for-each select="sdd:States/sdd:StateDefinition">
      <sdd:State>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="name">
          <xsl:value-of select="sdd:Representation/sdd:Label"/>
        </xsl:attribute>
        <xsl:attribute name="position">
          <xsl:value-of select="position()"/>
        </xsl:attribute>
        <xsl:attribute name="character">
		  <xsl:value-of select="$character/@position"/>
        </xsl:attribute>
      </sdd:State>
      </xsl:for-each>
    </xsl:for-each>
 </xsl:variable>

  <!-- root element: root -->
  <xsl:template match="/"><xsl:apply-templates select="sdd:Datasets"/></xsl:template>


  <xsl:template match="sdd:Dataset">   	
    <xsl:text>{"title":"</xsl:text>
    <xsl:value-of select="sdd:Representation/sdd:Label"/>
    <xsl:text>","imagePath":"</xsl:text>
    <xsl:value-of select="$imagePath"/><xsl:text>",</xsl:text>
    <xsl:apply-templates select="sdd:TaxonNames"/><xsl:text>,</xsl:text>
    <xsl:apply-templates select="sdd:DescriptiveConcepts"/>
    <xsl:apply-templates select="sdd:CharacterTrees/sdd:CharacterTree"/>
    <xsl:apply-templates select="sdd:IdentificationKeys/sdd:IdentificationKey"/>
    <xsl:apply-templates select="sdd:Characters"/><xsl:text>}</xsl:text>
  </xsl:template>
  
  <xsl:template match="sdd:IdentificationKey">
    
    <xsl:text>"leads":[{"question":"</xsl:text>
      <xsl:value-of select="sdd:Question/sdd:Text"/>
      <xsl:text>","id":"0","children":[</xsl:text>
      <xsl:for-each select="sdd:Leads/sdd:Lead[not(sdd:Parent)]">
        <xsl:variable name="childRef" select="@id"/>
        <xsl:variable name="child" select="exsl:node-set($lead-indexes)/sdd:Lead[@id = $childRef]"/>
        <xsl:value-of select="$child/@position"/>
        <xsl:if test="position() != last()">
		  <xsl:text>,</xsl:text>
		</xsl:if>
      </xsl:for-each>
      <xsl:text>]},</xsl:text>
      <xsl:for-each select="sdd:Leads/sdd:Lead">
        <xsl:text>{</xsl:text>
        <xsl:apply-templates select="."/>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() != last()">
		  <xsl:text>,</xsl:text>
		</xsl:if>
      </xsl:for-each>    
    <xsl:text>]</xsl:text>
  </xsl:template>
  
  <xsl:template match="sdd:Lead">
    <xsl:text>"id":"</xsl:text>
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="lead" select="exsl:node-set($lead-indexes)/sdd:Lead[@id = $id]"/>
    <xsl:value-of select="$lead/@position"/>
    <xsl:text>"</xsl:text>
    <xsl:if test="/sdd:Datasets/sdd:Dataset/sdd:IdentificationKeys/sdd:IdentificationKey/sdd:Leads/sdd:Lead[sdd:Parent/@ref = $id]">
      <xsl:text>,"children":[</xsl:text>
      <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:IdentificationKeys/sdd:IdentificationKey/sdd:Leads/sdd:Lead[sdd:Parent/@ref = $id]">
        <xsl:variable name="childRef" select="@id"/>
        <xsl:variable name="child" select="exsl:node-set($lead-indexes)/sdd:Lead[@id = $childRef]"/>
        <xsl:value-of select="$child/@position"/>
        <xsl:if test="position() != last()">
		  <xsl:text>,</xsl:text>
		</xsl:if>
      </xsl:for-each>
      <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:if test="sdd:MediaObject">
      <xsl:text>,"images":[</xsl:text>
      <xsl:for-each select="sdd:MediaObject">
        <xsl:variable name="ref" select="@ref"/>
        <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$ref]"/>
        <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
      </xsl:for-each>
      <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:if test="sdd:Question">
      <xsl:text>,"question":"</xsl:text>
      <xsl:value-of select="sdd:Question/sdd:Text"/>
      <xsl:text>"</xsl:text>
    </xsl:if>
    <xsl:if test="sdd:Statement">
      <xsl:text>,"statement":"</xsl:text>
      <xsl:value-of select="sdd:Statement"/>
      <xsl:text>"</xsl:text>
    </xsl:if>
    <xsl:if test="sdd:Parent/@ref">
      <xsl:text>,"parent":"</xsl:text>
      <xsl:variable name="parentRef" select="sdd:Parent/@ref"/>
      <xsl:variable name="parent" select="exsl:node-set($lead-indexes)/sdd:Lead[@id = $parentRef]"/>
      <xsl:value-of select="$parent/@position"/>
      <xsl:text>"</xsl:text>
    </xsl:if>
    <xsl:if test="sdd:TaxonName/@ref">
      <xsl:text>,"taxon":"</xsl:text>
      <xsl:variable name="ref" select="sdd:TaxonName/@ref"/>
      <xsl:variable name="taxon" select="exsl:node-set($taxon-indexes)/sdd:TaxonName[@id = $ref]"/>
      <xsl:value-of select="$taxon/@position"/>
      <xsl:text>"</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="sdd:CharacterTree">
    <xsl:text>"characterTree":[</xsl:text>
      <xsl:call-template name="treenodes">
        <xsl:with-param name="nodes" select="sdd:Nodes/*[not(sdd:Parent)]"/>
        <xsl:with-param name="allNodes" select="sdd:Nodes/*"/>
      </xsl:call-template>
    <xsl:text>],</xsl:text>
  </xsl:template>

  <xsl:template name="treenodes">
    <xsl:param name="nodes"/>
    <xsl:param name="allNodes"/>
    <xsl:for-each select="$nodes">
      <xsl:text>{</xsl:text>
      <xsl:choose>
        <xsl:when test="name()='CharNode'">
          <xsl:text>"character":"</xsl:text>
          <xsl:variable name="char" select="sdd:Character/@ref"/>
          <xsl:variable name="character" select="exsl:node-set($character-indexes)/sdd:Character[@id = $char]"/>
          <xsl:value-of select="$character/@position"/>
          <xsl:text>"</xsl:text>
          <xsl:if test="sdd:DependencyRules/sdd:OnlyApplicableIf">
            <xsl:text>,"onlyApplicableIf":[</xsl:text>
              <xsl:for-each select="sdd:DependencyRules/sdd:OnlyApplicableIf/sdd:State">
			    <xsl:call-template name="applicableState">
                  <xsl:with-param name="state" select="."/>
                </xsl:call-template>
                <xsl:if test="position() != last()">
		          <xsl:text>,</xsl:text>
		        </xsl:if>
              </xsl:for-each>
            <xsl:text>]</xsl:text>
          </xsl:if>
          <xsl:if test="sdd:DependencyRules/sdd:InapplicableIf">
            <xsl:text>,"inapplicableIf":[</xsl:text>
              <xsl:for-each select="sdd:DependencyRules/sdd:InapplicableIf/sdd:State">
			    <xsl:call-template name="applicableState">
                  <xsl:with-param name="state" select="."/>
                </xsl:call-template>
                <xsl:if test="position() != last()">
		          <xsl:text>,</xsl:text>
		        </xsl:if>
              </xsl:for-each>
            <xsl:text>]</xsl:text>
          </xsl:if>
          <xsl:text>,"type":"Character"</xsl:text>
        </xsl:when>
        <xsl:when test="name()='Node'">
          <xsl:variable name="dc" select="sdd:DescriptiveConcept/@ref"/>
          <xsl:variable name="descriptiveConcept" select="exsl:node-set($descriptiveConcept-indexes)/sdd:DescriptiveConcept[@id = $dc]"/>
          <xsl:text>"concept":"</xsl:text>
          <xsl:value-of select="$descriptiveConcept/@name"/>          
          <xsl:text>","type":"DescriptiveConcept"</xsl:text>
          <xsl:text>,"id":"</xsl:text>
          <xsl:value-of select="$descriptiveConcept/@position"/>          
          <xsl:text>"</xsl:text>
          <xsl:variable name="id" select="@id"/>
          <xsl:if test="/sdd:Datasets/sdd:Dataset/sdd:DescriptiveConcepts/sdd:DescriptiveConcept[@id=$dc]/sdd:Representation/sdd:MediaObject">
            <xsl:text>,"images":[</xsl:text>
            <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:DescriptiveConcepts/sdd:DescriptiveConcept[@id=$dc]/sdd:Representation/sdd:MediaObject">
              <xsl:variable name="ref" select="@ref"/>
              <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$ref]"/>
              <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
            </xsl:for-each>
            <xsl:text>]</xsl:text>
          </xsl:if>
          <xsl:if test="exsl:node-set($allNodes)[sdd:Parent/@ref = $id]">
            <xsl:text>,"children":[</xsl:text>            
            <xsl:call-template name="treenodes">
              <xsl:with-param name="nodes" select="exsl:node-set($allNodes)[sdd:Parent/@ref = $id]"/>
              <xsl:with-param name="allNodes" select="$allNodes"/>
            </xsl:call-template>
            <xsl:text>]</xsl:text>
          </xsl:if>
        </xsl:when>
      </xsl:choose>
      <xsl:text>}</xsl:text>
      <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="applicableState">
	<xsl:param name="state"/>
	<xsl:variable name="stateId" select="@ref"/>
	<xsl:variable name="stateLookup" select="exsl:node-set($state-indexes)/sdd:State[@id = $stateId]"/>
	<xsl:text>{"character":"</xsl:text>
	  <xsl:value-of select="$stateLookup/@character"/>
	<xsl:text>","state":"</xsl:text>
	  <xsl:value-of select="$stateLookup/@position"/>
	<xsl:text>"}</xsl:text>
  </xsl:template>

 <xsl:template match="sdd:DescriptiveConcepts">
   <xsl:text>"descriptiveConcepts":[</xsl:text>
   <xsl:for-each select="sdd:DescriptiveConcept">
      <xsl:text>{"id":"</xsl:text>
      <xsl:variable name="id" select="@id"/>
      <xsl:variable name="descriptiveConcept" select="exsl:node-set($descriptiveConcept-indexes)/sdd:DescriptiveConcept[@id = $id]"/>
      <xsl:value-of select="$descriptiveConcept/@position"/>
      <xsl:text>","concept":"</xsl:text>
      <xsl:value-of select="$descriptiveConcept/@name"/>
      <xsl:text>"</xsl:text>
      <xsl:if test="sdd:Representation/sdd:MediaObject">
        <xsl:text>,"images":[</xsl:text>
        <xsl:for-each select="sdd:Representation/sdd:MediaObject">
          <xsl:variable name="ref" select="@ref"/>
          <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$ref]"/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
        <xsl:text>]</xsl:text>
      </xsl:if>
      <xsl:text>,"children":[</xsl:text>
      <xsl:variable name="conceptNode" select="/sdd:Datasets/sdd:Dataset/sdd:CharacterTrees/sdd:CharacterTree/sdd:Nodes/sdd:Node[sdd:DescriptiveConcept/@ref=$id]"/>
      <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:CharacterTrees/sdd:CharacterTree/sdd:Nodes/*[sdd:Parent/@ref=$conceptNode/@id]">
        <xsl:text>{</xsl:text>
        <xsl:choose>
          <xsl:when test="name()='CharNode'">
            <xsl:text>"character":"</xsl:text>
            <xsl:variable name="char" select="sdd:Character/@ref"/>
            <xsl:variable name="character" select="exsl:node-set($character-indexes)/sdd:Character[@id = $char]"/>
            <xsl:value-of select="$character/@position"/>
            <xsl:text>","type":"Character"</xsl:text>
          </xsl:when>
          <xsl:when test="name()='Node'">
            <xsl:variable name="cc" select="sdd:DescriptiveConcept/@ref"/>
            <xsl:variable name="childConcept" select="exsl:node-set($descriptiveConcept-indexes)/sdd:DescriptiveConcept[@id = $cc]"/>
            <xsl:text>"concept":"</xsl:text>
            <xsl:value-of select="$childConcept/@name"/>          
            <xsl:text>","type":"DescriptiveConcept"</xsl:text>
            <xsl:text>,"id":"</xsl:text>
            <xsl:value-of select="$childConcept/@position"/>          
            <xsl:text>"</xsl:text>
            <xsl:if test="/sdd:Datasets/sdd:Dataset/sdd:DescriptiveConcepts/sdd:DescriptiveConcept[@id=$cc]/sdd:Representation/sdd:MediaObject">
            <xsl:text>,"images":[</xsl:text>
            <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:DescriptiveConcepts/sdd:DescriptiveConcept[@id=$cc]/sdd:Representation/sdd:MediaObject">
              <xsl:variable name="ref" select="@ref"/>
              <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$ref]"/>
              <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
            </xsl:for-each>
            <xsl:text>]</xsl:text>
          </xsl:if>
          </xsl:when>
        </xsl:choose>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
      </xsl:for-each>

      <xsl:text>]}</xsl:text>
      <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
   </xsl:for-each>
   <xsl:text>],</xsl:text>
 </xsl:template>

	<xsl:template match="sdd:TaxonNames">
		<xsl:text>"taxa":[</xsl:text>
		<xsl:choose>
			<xsl:when test="/sdd:Datasets/sdd:Dataset/sdd:CodedDescriptions">
				<xsl:for-each
					select="/sdd:Datasets/sdd:Dataset/sdd:CodedDescriptions/sdd:CodedDescription">
					<xsl:variable name="ref" select="sdd:Scope/sdd:TaxonName/@ref" />
					<xsl:variable name="description" select="." />
					<xsl:text>{</xsl:text>
					<xsl:apply-templates
						select="/sdd:Datasets/sdd:Dataset/sdd:TaxonNames/sdd:TaxonName[@id=$ref]" />
					<xsl:text>}</xsl:text>
					<xsl:if test="position() != last()">
						<xsl:text>,</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="sdd:TaxonName">
					<xsl:text>{</xsl:text>
					<xsl:apply-templates select="." />
					<xsl:text>}</xsl:text>
					<xsl:if test="position() != last()">
						<xsl:text>,</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:text>]</xsl:text>
	</xsl:template>

  <xsl:template match="sdd:TaxonName">
    <xsl:text>"id":"</xsl:text>
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="taxon" select="exsl:node-set($taxon-indexes)/sdd:TaxonName[@id = $id]"/>
    <xsl:value-of select="$taxon/@position"/>
    <xsl:text>","name":"</xsl:text>
    <xsl:value-of select="sdd:Representation/sdd:Label"/>
    <xsl:text>"</xsl:text>
    <xsl:if test="sdd:Representation/sdd:Detail">
      <xsl:text>,"description":"</xsl:text>
      <xsl:call-template name="escape-quot">
        <xsl:with-param name="string" select="sdd:Representation/sdd:Detail"/>
      </xsl:call-template>
      <xsl:text>"</xsl:text>
    </xsl:if>
    <xsl:variable name="media" select="sdd:Representation/sdd:MediaObject/@ref"/>
    <xsl:if test="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$media and sdd:Type='Image']">
      <xsl:text>,"images":[</xsl:text>        
        <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$media and sdd:Type='Image']">
          <xsl:apply-templates select="."/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
        <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:if test="exsl:node-set($taxon-map)/sdd:TaxonNames/sdd:TaxonName[@id = $id]">
      <xsl:text>,"links":[{"href":"</xsl:text>
        <xsl:value-of select="$linkPath"/>
        <xsl:value-of select="exsl:node-set($taxon-map)/sdd:TaxonNames/sdd:TaxonName[@id = $id]/@debuglabel"/>
        <xsl:text>","title":"</xsl:text>
        <xsl:value-of select="sdd:Representation/sdd:Label"/>
        <xsl:text>"}]</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="sdd:Characters">
    <xsl:text>"characters":[</xsl:text><xsl:apply-templates select="sdd:CategoricalCharacter|sdd:QuantitativeCharacter"/><xsl:text>]</xsl:text>
  </xsl:template>

  <xsl:template match="sdd:CategoricalCharacter">
    <xsl:text>{"name":"</xsl:text>
    <xsl:value-of select="sdd:Representation/sdd:Label"/>
    <xsl:text>","type":"UM", "id":"</xsl:text>
    <xsl:variable name="char" select="@id"/>
    <xsl:variable name="character" select="exsl:node-set($character-indexes)/sdd:Character[@id = $char]"/>
    <xsl:value-of select="$character/@position"/>
    <xsl:text>"</xsl:text>
    <xsl:if test="sdd:Representation/sdd:MediaObject">
        <xsl:text>,"images":[</xsl:text>
        <xsl:for-each select="sdd:Representation/sdd:MediaObject">
          <xsl:variable name="id" select="@ref"/>
          <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$id]"/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
        <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:text>,"states":[</xsl:text>
    <xsl:apply-templates select="sdd:States/sdd:StateDefinition"/>
    <xsl:text>], "data": [</xsl:text>      
      <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:CodedDescriptions/sdd:CodedDescription">
          <xsl:call-template name="stateData">
            <xsl:with-param name="char" select="$char"/>
            <xsl:with-param name="description" select="."/>
          </xsl:call-template>
        <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
      </xsl:for-each>
    <xsl:text>]}</xsl:text>
    <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
  </xsl:template>

  <xsl:template match="sdd:StateDefinition">
     <xsl:text>{"name":"</xsl:text>
     <xsl:value-of select="sdd:Representation/sdd:Label"/>
     <xsl:text>"</xsl:text>
     <xsl:if test="sdd:Representation/sdd:MediaObject">
        <xsl:text>,"images":[</xsl:text>
        <xsl:for-each select="sdd:Representation/sdd:MediaObject">
          <xsl:variable name="id" select="@ref"/>
          <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$id]"/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
        <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:text>}</xsl:text>
    <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
  </xsl:template>

  <xsl:template name="stateData">
    <xsl:param name="char"/>
    <xsl:param name="description"/>
    <xsl:choose>
      <xsl:when test="$description/sdd:SummaryData/sdd:Categorical[@ref=$char]">
        <xsl:apply-templates select="$description/sdd:SummaryData/sdd:Categorical[@ref=$char]"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>0</xsl:text></xsl:otherwise>
    </xsl:choose>    
  </xsl:template>

  <xsl:template match="sdd:Categorical">
    <xsl:variable name="states" select="sdd:State"/>
    <xsl:choose>
      <xsl:when test="count($states) = 0">
        <xsl:text>0</xsl:text>
      </xsl:when>      
      <xsl:when test="count($states) = 1">
        <xsl:apply-templates select="$states"/>
      </xsl:when>
      <xsl:otherwise>
       <xsl:text>[</xsl:text>
        <xsl:for-each select="$states">
          <xsl:apply-templates select="."/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
       <xsl:text>]</xsl:text>
      </xsl:otherwise>
    </xsl:choose>   
  </xsl:template>

  <xsl:template match="sdd:State">
    <xsl:variable name="id" select="@ref"/>
    <xsl:variable name="state" select="exsl:node-set($state-indexes)/sdd:State[@id = $id]"/>
    <xsl:value-of select="$state/@position"/>
  </xsl:template>

  <xsl:template match="sdd:QuantitativeCharacter">
    <xsl:text>{"name":"</xsl:text>
    <xsl:value-of select="sdd:Representation/sdd:Label"/>
    <xsl:text>","type":"RN","id":"</xsl:text>
    <xsl:variable name="char" select="@id"/>
    <xsl:variable name="character" select="exsl:node-set($character-indexes)/sdd:Character[@id = $char]"/>
    <xsl:value-of select="$character/@position"/>
    <xsl:text>"</xsl:text>
    <xsl:if test="sdd:Representation/sdd:MediaObject">
        <xsl:text>,"images":[</xsl:text>
        <xsl:for-each select="sdd:Representation/sdd:MediaObject">
          <xsl:variable name="id" select="@ref"/>
          <xsl:apply-templates select="/sdd:Datasets/sdd:Dataset/sdd:MediaObjects/sdd:MediaObject[@id=$id]"/>
          <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
        </xsl:for-each>
        <xsl:text>]</xsl:text>
    </xsl:if>
    <xsl:text>, "data": [</xsl:text>
      <xsl:for-each select="/sdd:Datasets/sdd:Dataset/sdd:CodedDescriptions/sdd:CodedDescription">
          <xsl:call-template name="quantData">
            <xsl:with-param name="char" select="$char"/>
            <xsl:with-param name="description" select="."/>
          </xsl:call-template>
        <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
      </xsl:for-each>
    <xsl:variable name="values" select="/sdd:Datasets/sdd:Dataset/sdd:CodedDescriptions/sdd:CodedDescription/sdd:SummaryData/sdd:Quantitative[@ref=$char]/sdd:Measure/@value[. != 'NaN']"/>
    <xsl:text>],"min":"</xsl:text>
    <xsl:value-of select="math:min($values)"/>
    <xsl:text>","max":"</xsl:text>
    <xsl:value-of select="math:max($values)"/>
    <xsl:text>","unit":"</xsl:text>
    <xsl:value-of select="sdd:MeasurementUnit/sdd:Label"/>
    <xsl:text>"}</xsl:text>
    <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
  </xsl:template>

  <xsl:template name="quantData">
    <xsl:param name="char"/>
    <xsl:param name="description"/>
    <xsl:choose>
      <xsl:when test="$description/sdd:SummaryData/sdd:Quantitative[@ref=$char]">
        <xsl:apply-templates select="$description/sdd:SummaryData/sdd:Quantitative[@ref=$char]"/>
      </xsl:when>
      <xsl:otherwise><xsl:text>0</xsl:text></xsl:otherwise>
    </xsl:choose>    
  </xsl:template>

  <xsl:template match="sdd:Quantitative">
    <xsl:text>"</xsl:text>
    <xsl:apply-templates select="sdd:Measure[@type = 'Min']"/>
    <xsl:text>-</xsl:text>
    <xsl:apply-templates select="sdd:Measure[@type = 'Max']"/>
    <xsl:text>"</xsl:text>
  </xsl:template>

  <xsl:template match="sdd:Measure">
    <xsl:value-of select="@value"/>
  </xsl:template>

  <xsl:template match="sdd:MediaObject">
    <xsl:variable name="id" select="@id"/>
      <xsl:choose>
        <xsl:when test="sdd:Type/text() = 'Image'">
          <xsl:text>{"href":"</xsl:text>
          <xsl:if test="exsl:node-set($image-map)/sdd:MediaObjects/sdd:MediaObject[@id = $id]">      
            <xsl:value-of select="exsl:node-set($image-map)/sdd:MediaObjects/sdd:MediaObject[@id = $id]/@debuglabel"/>
          </xsl:if>
           <xsl:text>","caption":"</xsl:text>
           <xsl:value-of select="sdd:Representation/sdd:Label"/>
           <xsl:if test="sdd:Representation/sdd:Detail">
              <xsl:text>","description":"</xsl:text>
              <xsl:call-template name="escape-quot">
                <xsl:with-param name="string" select="sdd:Representation/sdd:Detail"/>
              </xsl:call-template>
           </xsl:if>
          <xsl:text>"}</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>{"href":"</xsl:text>
           <xsl:value-of select="sdd:Source/@href"/>
           <xsl:text>","title":"</xsl:text>
           <xsl:value-of select="sdd:Representation/sdd:Label"/>           
          <xsl:text>"}</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="escape-quot">
    <xsl:param name="string"/>
    <xsl:variable name="quot" select="'&quot;'" />
    <xsl:choose>
      <xsl:when test='contains($string, $quot)'>
        <xsl:value-of select="translate(substring-before($string,$quot), '&#x20;&#x9;&#xD;&#xA;', ' ')" />
        <xsl:text>'</xsl:text>
	<xsl:call-template name="escape-quot">
          <xsl:with-param name="string" select="translate(substring-after($string,$quot), '&#x20;&#x9;&#xD;&#xA;', ' ')" />
   	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="translate($string, '&#x20;&#x9;&#xD;&#xA;', ' ')" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
