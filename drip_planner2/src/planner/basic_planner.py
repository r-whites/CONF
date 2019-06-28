import json
import operator
import pdb
import re
from toscaparser.tosca_template import ToscaTemplate
from toscaparser.topology_template import TopologyTemplate
import toscaparser.utils.yamlparser
import urllib
import urllib.parse
import sys
import pdb



class BasicPlanner:

    
    def __init__(self, path):
        self.template = ToscaTemplate(path)
        print("Description:"+self.template.description)
        for node in self.template.nodetemplates:
            print("Node: "+node.name+" Type:"+node.type+" Req: "+str(node.requirements))
#            print(node.get_properties().keys())
#            print(node.get_capabilities().keys)
        
            
            
        
    def target_meets_requirement(self, target_relationship_element, requirement):
        node = self.get_node(target_relationship_element)
        supertypes = self.get_super_types(node['type'], None)
        for node_type in supertypes:
            if 'capabilityDefinitions' in node_type:
                for cap in node_type['capabilityDefinitions']['capabilityDefinition']:
                    if cap['capabilityType'] == requirement:
                        return True
                        
        return False
        
    def get_relationship_of_source_node(self, source_id, relationships):
        for rel in relationships:
            if(rel['sourceElement']['ref'] == source_id):
                return rel


    def get_node(self, node_type_id):
        for node in self.node_templates:
            if node['id'] == node_type_id:
                return node
        
    def get_all_requirements(self, node_templates):
        requirements = {}
        for node_template_name in node_templates:
            node_template = node_templates[node_template_name]
            node_type = node_template['type']
            if 'requirements' in node_template:
                requirements[node_template_name] = node_template['requirements']
            self.get_super_types_requirements(node_type, None)
#            id = node_template['id']
#            req = self.get_requirements(node_template)
#            if(req):
#                for r in req['requirement']:
#                    all_requirements.append(r['type'])
#            self.requirements[id] = all_requirements
            
    
    def get_unmet_requirements(self, requirements,relationships):
        unmet_requirements = {}
        for node_name in requirements:
            relationship = self.get_relationship_of_source_node(node_name, relationships)
            if relationship:
                for requirement in requirements[node_name]:
                    if (not self.target_meets_requirement(relationship['targetElement']['ref'], requirement)):
                        unmet_requirements[node_name] = requirement
            else:
                for requirement in requirements[node_name]:
                    unmet_requirements[node_name] = requirement
        return unmet_requirements
    
    
    def get_condiate_nodes(self,unmet_requirements):
        condiate_nodes = []
        for node_name in unmet_requirements:
            node_types = self.get_node_types_with_capability(unmet_requirements[node_name])
            print(node_types)
            
            
    def get_node_types_with_capability(self,requirement_qname):
        regex = r"\{(.*?)\}"
        matches = re.finditer(regex, requirement_qname, re.MULTILINE | re.DOTALL)
        namespace = next(matches).group(1)
        req_id = requirement_qname.replace("{" + namespace + "}", "")  
        
        if not self.all_node_types:
            servicetemplate_url = self.tosca_reposetory_api_base_url + "/nodetypes/"
            header = {'accept': 'application/json'}
            req = urllib.request.Request(url=servicetemplate_url, headers=header, method='GET')
            res = urllib.request.urlopen(req, timeout=5)
            res_body = res.read()
            self.all_node_types = json.loads(res_body.decode("utf-8"))  
        matching_nodes = {}
        for node in self.all_node_types:
            supertypes = self.get_super_types(node['qName'],None)
            for node in supertypes:
                if 'capabilityDefinitions' in node:
                    for cap in node['capabilityDefinitions']['capabilityDefinition']:
                        cap_qname = cap['capabilityType']
                        cap_matches = re.finditer(regex, cap_qname, re.MULTILINE | re.DOTALL)
                        namespace = next(cap_matches).group(1)
                        cap_id = cap_qname.replace("{" + namespace + "}", "")
                        if cap_id == req_id and node['name'] not in matching_nodes:
                            matching_nodes[node['name']] = node
                            break
        return matching_nodes.values()
        
            
    def get_all_relationships(self, dict_tpl):
        all_relationships = []
        service_templates = self.get_service_template(dict_tpl)
        for service in service_templates:
            topology_template = self.get_topology_template(service)
            relationships = self.get_relationships(topology_template)
            for rel in relationships:
                all_relationships.append(rel)
        return all_relationships
                
    def get_super_types(self, type, supertypes):  
        print(type)
        
                
    def get_super_types_requirements(self, node_type, requirements):
        print(node_type)
        
        
    def get_service_template(self, dict_tpl): 
        if(not self.service_templates):
            self.service_templates = self.find(dict_tpl, self.service_template_names)
        return self.service_templates
    
    def get_topology_template(self, dict_tpl):  
        if (not self.topology_templates):
            self.topology_templates = self.find(dict_tpl, self.topology_template_names)
        return self.topology_templates
    
    def get_node_templates(self, dict_tpl):
        if (not self.node_templates):
            self.node_templates = self.find(dict_tpl, self.node_template_names)  
        return self.node_templates
    
    def get_requirements(self, dict_tpl):  
        return self.find(dict_tpl, self.requirement_names)   
    
    def get_relationships(self, dict_tpl):  
        return self.find(dict_tpl, self.relationships_names)
    
    def get_requirement_type(self, dict_req):
        return self.find(dict_req, self.type_names)
    
    def find(self, dict_tpl, names):
        if dict_tpl:
            for name in names:
                if(name in dict_tpl):
                    return dict_tpl[name]