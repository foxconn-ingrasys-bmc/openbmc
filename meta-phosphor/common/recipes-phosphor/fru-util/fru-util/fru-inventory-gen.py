#!/usr/bin/python -u

import os
import sys
import dbus
import subprocess
from obmc.dbuslib.bindings import get_dbus

bus = get_dbus()

INVENTORY_MANAGER_OBJECT_PATH = '/xyz/openbmc_project/inventory'
INVENTORY_MANAGER_DBUS_BUS_NAME = 'xyz.openbmc_project.Inventory.Manager'
INVENTORY_MANAGER_DBUS_INTF_NAME = 'xyz.openbmc_project.Inventory.Manager'
INVENTORY_OCS_FRU_COMMAND = 'ocs-fru -c %d -s %x -r'
INVENTORY_OBJECT_PATH_ROOT = '/xyz/openbmc_project/inventory'

class EM_FRU_INVENTORY_INTERFACE_NAME:
    EM_EM_FRU_INVENTORY_CHASSIS_INTERFACE = 'xyz.openbmc_project.Inventory.FoxconnFruItem.Chassis'
    EM_EM_FRU_INVENTORY_BOARD_INTERFACE = 'xyz.openbmc_project.Inventory.FoxconnFruItem.Board'
    EM_EM_FRU_INVENTORY_PRODUCT_INTERFACE = 'xyz.openbmc_project.Inventory.FoxconnFruItem.Product'

class EM_FRU_EEPROM_PRODUCT_TYPE:
    EM_FRU_EEPROM_AT24C64 = 0
    EM_FRU_EEPROM_AT24C02 = 1

g_fru_inventory_property_interface = {
    EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_CHASSIS_INTERFACE: {
    # @property name: define on xyz/openbmc_project/Inventory/FoxconnFruItem/Chassis.interface.yaml with phosphor-dbus-interfaces.git
    # @ocs-fru relative property message string: define on fru_field_name of FRU_AREA_INFO_FIELD in fru.c with fru-util.git
    #
    #   property name               ocs-fru relative property message string
    #   -------------------         ----------------------------------------
        'ChassisPartNumber':        'Chassis Part Number String',
        'ChassisSerialNumber':      'Chassis Serial Number String',
        'ChassisExtra1UUID':        'Chassis Extra 1 UUID String',
        'ChassisCustomInformation': 'Chassis Custom information',
    },
    EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_BOARD_INTERFACE: {
    # @property name: define on xyz/openbmc_project/Inventory/FoxconnFruItem/Board.interface.yaml with phosphor-dbus-interfaces.git
    # @ocs-fru relative property message string: define on fru_field_name of FRU_AREA_INFO_FIELD in fru.c with fru-util.git
    #
    #   property name               ocs-fru relative property message string
    #   -------------------         ----------------------------------------
        'BoardManufacturingDate':   'Board Manufacturing Date/Time',
        'BoardManufacturer':        'Board Manufacturer String',
        'BoardProductName':         'Board Product Name String',
        'BoardSerialNumber':        'Board Serial Number String',
        'BoardPartNumber':          'Board Part Number String',
        'BoardFRUFileID':           'Board FRU File ID String',
        'BoardCustomInformation':   'Board Custom information',
    },
    EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_PRODUCT_INTERFACE: {
    # @property name: define on xyz/openbmc_project/Inventory/FoxconnFruItem/Product.interface.yaml with phosphor-dbus-interfaces.git
    # @ocs-fru relative property message string: define on fru_field_name of FRU_AREA_INFO_FIELD in fru.c with fru-util.git
    #
    #   property name               ocs-fru relative property message string
    #   -------------------         ----------------------------------------
        'ProductManufacturerName':  'Product Manufacturer Name String',
        'ProductName':              'Product Product Name String',
        'ProductPartNumber':        'Product Part/Model Number String',
        'ProductVersion':           'Product Version String',
        'ProductSerialNumber':      'Product Serial Number String',
        'ProductAssetTag':          'Product Asset Tag String',
        'ProductFRUFileID':         'Product FRU File ID String',
        'ProductExtraRackID':       'Product Extra Rack ID String',
        'ProductExtraNodeID':       'Product Extra Node ID String',
        'ProductCustomInformation': 'Product Custom information',
    },
}

def _add_inventory_motherboard(configs):
    objpath = INVENTORY_OBJECT_PATH_ROOT + '/system/chassis/motherboard'
    config = {
        'FRU_I2C_BUS': 0,
        'FRU_I2C_SLAVE': 0x50,
        'FRU_INVENTORY_INTERFACES':
            [
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_CHASSIS_INTERFACE,
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_BOARD_INTERFACE,
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_PRODUCT_INTERFACE
            ],
        }
    if objpath in configs:
        configs[objpath].append(config)
    else:
        configs[objpath] = []
        configs[objpath].append(config)

def _add_inventory_EXP(configs):
    objpath = INVENTORY_OBJECT_PATH_ROOT + '/system/chassis/EXP'
    config = {
        'FRU_I2C_BUS': 9,
        'FRU_I2C_SLAVE': 0x51,
        'FRU_EEPROM_PRODUCT_TYPE': EM_FRU_EEPROM_PRODUCT_TYPE.EM_FRU_EEPROM_AT24C02,
        'FRU_INVENTORY_INTERFACES':
            [
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_BOARD_INTERFACE,
            ],
        }
    if objpath in configs:
        configs[objpath].append(config)
    else:
        configs[objpath] = []
        configs[objpath].append(config)

def _add_inventory_FPDB(configs):
    objpath = INVENTORY_OBJECT_PATH_ROOT + '/system/chassis/FPDB'
    config = {
        'FRU_I2C_BUS': 9,
        'FRU_I2C_SLAVE': 0x52,
        'FRU_EEPROM_PRODUCT_TYPE': EM_FRU_EEPROM_PRODUCT_TYPE.EM_FRU_EEPROM_AT24C02,
        'FRU_INVENTORY_INTERFACES':
            [
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_BOARD_INTERFACE,
            ],
        }
    if objpath in configs:
        configs[objpath].append(config)
    else:
        configs[objpath] = []
        configs[objpath].append(config)

def _add_inventory_BP(configs):
    objpath = INVENTORY_OBJECT_PATH_ROOT + '/system/chassis/BP'
    config = {
        'FRU_I2C_BUS': 9,
        'FRU_I2C_SLAVE': 0x55,
        'FRU_EEPROM_PRODUCT_TYPE': EM_FRU_EEPROM_PRODUCT_TYPE.EM_FRU_EEPROM_AT24C02,
        'FRU_INVENTORY_INTERFACES':
            [
                EM_FRU_INVENTORY_INTERFACE_NAME.EM_EM_FRU_INVENTORY_BOARD_INTERFACE,
            ],
        }
    if objpath in configs:
        configs[objpath].append(config)
    else:
        configs[objpath] = []
        configs[objpath].append(config)

g_fru_inventory_config = {}
_add_inventory_motherboard(g_fru_inventory_config)
_add_inventory_EXP(g_fru_inventory_config)
_add_inventory_FPDB(g_fru_inventory_config)
_add_inventory_BP(g_fru_inventory_config)

def execute_ocs_fru(config):
    try:
        fru_i2c_bus = config['FRU_I2C_BUS']
        fru_i2c_slave = config['FRU_I2C_SLAVE']
        ocs_fru_cmd = INVENTORY_OCS_FRU_COMMAND % (fru_i2c_bus, fru_i2c_slave)
        if 'FRU_EEPROM_PRODUCT_TYPE' in config:
            ocs_fru_cmd += " -fru_eeprom_product_type " + str(config['FRU_EEPROM_PRODUCT_TYPE'])
        ocs_fru_cmd_data = subprocess.check_output(ocs_fru_cmd, shell=True)
        return ocs_fru_cmd_data.replace('\x00', '') #skip garbage message
    except:
        print "Fru-inventory-gen: execute_ocs_fru fail:" + ocs_fru_cmd
        return ""

def parse_ocs_fru_data_mapping_inventory_property(ocs_fru_cmd_data, config):
    property_config = {}
    if 'FRU_INVENTORY_INTERFACES' not in config:
        return property_config
    for property_interface in config['FRU_INVENTORY_INTERFACES']:
        if property_interface not in g_fru_inventory_property_interface:
            continue
        property_config[property_interface] = {}
        for property_name in g_fru_inventory_property_interface[property_interface]:
            try:
                property_ocs_fru_idx = ocs_fru_cmd_data.find(g_fru_inventory_property_interface[property_interface][property_name])
                if property_ocs_fru_idx < 0:
                    continue
                property_value = ocs_fru_cmd_data[property_ocs_fru_idx:].split('\n')[0].split(':')[1]
                property_config[property_interface][property_name] = property_value
            except:
                print "Fru-inventory-gen: parse_ocs_fru_data_mapping_inventory_property fail:" + str(property_name)
    return property_config

def inventory_manager_notify_control(dbus_notify_parameters):
    try:
        dbus_object = bus.get_object(INVENTORY_MANAGER_DBUS_BUS_NAME, INVENTORY_MANAGER_OBJECT_PATH)
        dbus_interface = dbus.Interface(dbus_object, INVENTORY_MANAGER_DBUS_INTF_NAME)
        dbus_interface.Notify(dbus_notify_parameters)
    except:
        print "Fru-inventory-gen: inventory_manager_notify_control fail:"
        print dbus_notify_parameters

if __name__ == '__main__':
    print "Fru-inventory-gen  Start!!!"
    dbus_notify_parameters = {}
    for objectpath in g_fru_inventory_config:
        config = g_fru_inventory_config[objectpath][0]
        try:
            ocs_fru_cmd_data = execute_ocs_fru(config)
        except:
            print "Fru-inventory-gen: ocs-fru command error!!"
            continue
        property_config = parse_ocs_fru_data_mapping_inventory_property(ocs_fru_cmd_data, config)
        if len(property_config) == 0:
            continue
        #@dbus_notify_parameter_objectpath:
        #    Parameter about object path for 'Notify' control with Inventory Manager would
        # set default object path root as INVENTORY_OBJECT_PATH_ROOT. So, it is  needless for
        # INVENTORY_OBJECT_PATH_ROOT of objpath.
        dbus_notify_parameter_objectpath = objectpath.replace(INVENTORY_OBJECT_PATH_ROOT, '')
        dbus_notify_parameters[dbus_notify_parameter_objectpath] = property_config
    inventory_manager_notify_control(dbus_notify_parameters)
    print "Fru-inventory-gen  Finish!!!"
