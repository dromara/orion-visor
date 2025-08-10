<template>
  <a-spin :loading="loading">
    <!-- 表单 -->
    <a-descriptions :column="1" bordered>
      <!-- SN -->
      <a-descriptions-item label="SN" :span="2">
        <a-input v-if="editing"
                 v-model="formModel.sn"
                 class="input"
                 size="mini"
                 allow-clear />
        <span v-else class="text">{{ formModel.sn }}</span>
      </a-descriptions-item>
      <!-- 系统名称 -->
      <a-descriptions-item label="系统名称" :span="2">
        <a-input v-if="editing"
                 v-model="formModel.osName"
                 class="input"
                 size="mini"
                 allow-clear />
        <span v-else class="text">{{ formModel.osName }}</span>
      </a-descriptions-item>
      <!-- CPU型号 -->
      <a-descriptions-item label="CPU型号" :span="2">
        <a-input v-if="editing"
                 v-model="formModel.cpuModel"
                 class="input"
                 size="mini"
                 allow-clear />
        <span v-else class="text">{{ formModel.cpuModel }}</span>
      </a-descriptions-item>
      <!-- CPU核心数 -->
      <a-descriptions-item label="CPU核心数">
        <a-input-number v-if="editing"
                        v-model="formModel.cpuPhysicalCore"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear />
        <span v-else class="text">{{ formModel.cpuPhysicalCore }}</span>
      </a-descriptions-item>
      <!-- CPU线程数 -->
      <a-descriptions-item label="CPU线程数">
        <a-input-number v-if="editing"
                        v-model="formModel.cpuLogicalCore"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear />
        <span v-else class="text">{{ formModel.cpuLogicalCore }}</span>
      </a-descriptions-item>
      <!-- CPU速率 -->
      <a-descriptions-item label="CPU速率">
        <a-input-number v-if="editing"
                        v-model="formModel.cpuFrequency"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear>
          <template #suffix>
            GHz
          </template>
        </a-input-number>
        <span v-else class="text">{{ addSuffix(formModel.cpuFrequency, 'GHz') }}</span>
      </a-descriptions-item>
      <!-- 内存 -->
      <a-descriptions-item label="内存">
        <a-input-number v-if="editing"
                        v-model="formModel.memorySize"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear>
          <template #suffix>
            G
          </template>
        </a-input-number>
        <span v-else class="text">{{ addSuffix(formModel.memorySize, 'G') }}</span>
      </a-descriptions-item>
      <!-- 硬盘 -->
      <a-descriptions-item label="硬盘">
        <a-input-number v-if="editing"
                        v-model="formModel.diskSize"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear>
          <template #suffix>
            G
          </template>
        </a-input-number>
        <span v-else class="text">{{ addSuffix(formModel.diskSize, 'G') }}</span>
      </a-descriptions-item>
      <!-- 上行带宽 -->
      <a-descriptions-item label="上行带宽">
        <a-input-number v-if="editing"
                        v-model="formModel.inBandwidth"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear>
          <template #suffix>
            M
          </template>
        </a-input-number>
        <span v-else class="text">{{ addSuffix(formModel.inBandwidth, 'M') }}</span>
      </a-descriptions-item>
      <!-- 下行带宽 -->
      <a-descriptions-item label="下行带宽">
        <a-input-number v-if="editing"
                        v-model="formModel.outBandwidth"
                        class="input"
                        size="mini"
                        hide-button
                        allow-clear>
          <template #suffix>
            M
          </template>
        </a-input-number>
        <span v-else class="text">{{ addSuffix(formModel.outBandwidth, 'M') }}</span>
      </a-descriptions-item>
      <!-- 公网地址 -->
      <a-descriptions-item label="公网地址" :span="2">
        <a-input-tag v-model="formModel.publicIpAddresses"
                     :readonly="!editing"
                     class="input"
                     size="mini"
                     allow-clear />
      </a-descriptions-item>
      <!-- 私网地址 -->
      <a-descriptions-item label="私网地址" :span="2">
        <a-input-tag v-model="formModel.privateIpAddresses"
                     :readonly="!editing"
                     class="input"
                     size="mini"
                     allow-clear />
      </a-descriptions-item>
      <!-- 负责人 -->
      <a-descriptions-item label="负责人" :span="2">
        <a-input v-if="editing"
                 v-model="formModel.chargePerson"
                 class="input"
                 size="mini"
                 allow-clear />
        <span v-else class="text">{{ formModel.chargePerson }}</span>
      </a-descriptions-item>
      <!-- 创建时间 -->
      <a-descriptions-item label="创建时间" :span="2">
        <a-date-picker v-if="editing"
                       v-model="formModel.createdTime"
                       class="input"
                       size="mini"
                       placeholder=" "
                       allow-clear />
        <span v-else-if="formModel.createdTime" class="text">{{ dateFormat(new Date(formModel.createdTime), 'yyyy-MM-dd') }}</span>
        <span v-else class="text" />
      </a-descriptions-item>
      <!-- 到期时间 -->
      <a-descriptions-item label="到期时间" :span="2">
        <a-date-picker v-if="editing"
                       v-model="formModel.expiredTime"
                       value-format="timestamp"
                       class="input"
                       size="mini"
                       placeholder=" "
                       allow-clear />
        <span v-else-if="formModel.expiredTime" class="text">{{ dateFormat(new Date(formModel.expiredTime), 'yyyy-MM-dd') }}</span>
        <span v-else class="text" />
      </a-descriptions-item>
      <!-- 自定义规格 -->
      <template v-if="formModel.items?.length">
        <a-descriptions-item v-for="(item, index) in formModel.items"
                             :key="index"
                             :span="2">
          <template #label>
            <a-input v-if="editing"
                     v-model="item.label"
                     value-format="timestamp"
                     class="label"
                     size="mini"
                     allow-clear />
            <span v-else class="label">{{ item.label }}</span>
          </template>
          <!-- 值 -->
          <div class="item-wrapper">
            <a-input v-if="editing"
                     v-model="item.value"
                     class="input"
                     size="mini"
                     allow-clear />
            <span v-else class="text">{{ item.value }}</span>
            <!-- 移除 -->
            <a-button v-if="editing"
                      class="ml4"
                      size="mini"
                      type="text"
                      @click="removeSpec(index)">
              移除
            </a-button>
          </div>
        </a-descriptions-item>
      </template>
    </a-descriptions>
    <!-- 操作 -->
    <div class="actions">
      <!-- 编辑 -->
      <a-button v-if="!editing"
                type="primary"
                long
                @click="toggleEditing">
        编辑
      </a-button>
      <!-- 保存 -->
      <a-button v-if="editing"
                type="primary"
                long
                @click="saveSpec">
        保存
      </a-button>
      <!-- 取消 -->
      <a-button v-if="editing"
                class="extra-button"
                type="primary"
                long
                @click="fetchHostSpec">
        取消
      </a-button>
      <!-- 新增规格 -->
      <a-button v-if="editing"
                class="extra-button"
                type="primary"
                long
                @click="addSpec">
        新增规格
      </a-button>
    </div>
  </a-spin>
</template>

<script lang="ts">
  export default {
    name: 'hostFormSpec'
  };
</script>

<script lang="ts" setup>
  import type { HostSpecExtraModel } from '@/api/asset/host-extra';
  import { onMounted, ref } from 'vue';
  import { updateHostSpec } from '@/api/asset/host';
  import { getHostExtraItem } from '@/api/asset/host-extra';
  import { addSuffix, dateFormat } from '@/utils';
  import { useToggle } from '@vueuse/core';
  import useLoading from '@/hooks/loading';

  const props = defineProps<{
    hostId: number;
  }>();
  const emits = defineEmits(['updated']);

  const { loading, setLoading } = useLoading();
  const [editing, toggleEditing] = useToggle();

  const formModel = ref<HostSpecExtraModel>({} as HostSpecExtraModel);

  // 加载配置
  const fetchHostSpec = async () => {
    setLoading(true);
    editing.value = false;
    try {
      const { data } = await getHostExtraItem<HostSpecExtraModel>({ hostId: props.hostId, item: 'SPEC' });
      formModel.value = data;
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  // 添加规格
  const addSpec = () => {
    if (!formModel.value.items) {
      formModel.value.items = [];
    }
    formModel.value.items.push({ label: '', value: '' });
  };

  // 移除规格
  const removeSpec = (index: number) => {
    formModel.value.items.splice(index, 1);
  };

  // 保存规格
  const saveSpec = async () => {
    setLoading(true);
    try {
      // 设置额外配置的 key
      if (formModel.value.items?.length) {
        formModel.value.items.forEach(s => {
          s.key = s.label;
        });
      }
      // 更新
      await updateHostSpec({
        hostId: props.hostId,
        extra: JSON.stringify(formModel.value)
      });
      toggleEditing();
      emits('updated');
    } catch (e) {
    } finally {
      setLoading(false);
    }
  };

  onMounted(fetchHostSpec);

</script>

<style lang="less" scoped>
  :deep(.arco-descriptions-item-value) {
    padding: 6px 8px !important;
  }

  .label {
    display: flex;
    overflow: hidden;
  }

  .input, .text {
    min-height: 24px;
    font-size: 13px;
  }

  .text {
    display: flex;
  }

  .item-wrapper {
    display: flex;
    width: 306px;
  }

  .actions {
    width: 100%;
    margin-top: 16px;
    display: flex;
  }
</style>
