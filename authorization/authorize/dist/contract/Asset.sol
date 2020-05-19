pragma solidity ^0.4.25;

import "./Table.sol";

contract Asset {
    // event
    event applyTransactionEvent(String indexed node1,String indexed node2);
    event postTransactionEvent(String indexed node1,String indexed node2,file indexed file);
    event queryTransactionEvent(String indexed node1);
    event decryptTransactionEvent(String indexed node1);
    constructor() public {
        // 构造函数中创建t_asset表
        createTable();
    }

    function createTable() private {
        TableFactory tf = TableFactory(0x1001); 
        tf.createTable("token", "node1", "node2",,"authorize","token");
    }

    function openTable() private returns(Table) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("token");
        return table;
    }

    function queryTransaction(string node1) public constant returns(int256, uint256) {
        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(node1, table.newCondition());
        uint256 asset_value = 0;
        if (0 == uint256(entries.size())) {
            return (-1, asset_value);
        } else {
            Entry entry = entries.get(0);
            return (0, uint256(entry.getInt("asset_value")));
        }
    }

    function applyTransaction(string node1, string node2) public returns(int256){
        int256 ret_code = 0;
        int256 ret= 0;
        uint256 temp_asset_value = 0;
     
            Table table = openTable();
            
            Entry entry = table.newEntry();
            entry.set("node1", node1);
            entry.set("node2", node2);
            entry.set("authorize","1");
            // 插入
            int count = table.insert(token, entry);
            if (count == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        }

        emit applyTransactionEvent(ret_code, node1,node2);

        return ret_code;
    }

      function postTransactionEvent(string node1, string node2,File file) public returns(int256){
        int256 ret_code = 0;
        int256 ret= 0;
        uint256 temp_asset_value = 0;
     
            Table table = openTable();
            
            Entry entry = table.newEntry();
            entry.set("node1", node2);
            entry.set("node2", node1);
            entry.set("authorize","2");
           entry.set("file",file);
            // 插入
            int count = table.insert(token, entry);
            if (count == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        }

        emit postTransactionEvent(ret_code, node1,node2,file);

        return ret_code;
    }

   function decryptTransactionEvent(string node1) public returns(int256){
        int256 ret_code = 0;
        int256 ret= 0;
        uint256 temp_asset_value = 0;
     
            Table table = openTable();
            
            Entry entry = table.newEntry();
            entry.set("node1", node2);
            entry.set("authorize","3");
            // 插入
            int count = table.insert(token, entry);
            if (count == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        }

        emit decryptTransactionEvent(ret_code, node1);

        return ret_code;
    }
}