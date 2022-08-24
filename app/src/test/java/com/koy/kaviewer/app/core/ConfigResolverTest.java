//package com.koy.kaviewer.app.core;
//
//import org.junit.jupiter.api.Test;
//
//class ConfigResolverTest {
//    @Test
//    void test() {
//        final Solution solution = new Solution();
//    }
//
//    public class ListNode {
//        int val;
//        ListNode next;
//
//        ListNode() {
//        }
//
//        ListNode(int val) {
//            this.val = val;
//        }
//
//        ListNode(int val, ListNode next) {
//            this.val = val;
//            this.next = next;
//        }
//    }
//
//    class Solution {
//        public ListNode reverseList(ListNode head) {
//            ListNode pre = null;
//            ListNode current = head;
//            while (current != null && current.next != null) {
//                current.next = pre;
//                pre = current;
//                current = current.next;
//            }
//
//            return current;
//        }
//    }
//}