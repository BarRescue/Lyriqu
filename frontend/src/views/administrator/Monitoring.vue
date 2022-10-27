<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="entries"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar
          flat
        >
          <v-toolbar-title>New entries</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-dialog
            v-model="dialog"
            max-width="500px"
          >
            <v-card v-if="info">
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <strong>Song name</strong>
                      <p>{{ info.songName }}</p>
                    </v-col>
                    <v-col cols="12">
                      <strong>Song Audio</strong>
                      <p>
                        <a :href="info.songPath" target="_blank">View here</a>
                      </p>
                    </v-col>
                    <v-col cols="12">
                      <strong>Song Image</strong>
                      <p>
                        <a :href="info.imagePath" target="_blank">View here</a>
                      </p>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-text-field
                  v-model="reason"
                  label="Reason (Leave empty if approve)"
                ></v-text-field>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="approve"
                >
                  Approve
                </v-btn>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="deny"
                >
                  Deny
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:item.actions="{ item }">
          <v-icon
            small
            style="margin-right: 10px;"
            @click="getEntry(item)"
          >
            View entry
          </v-icon>
        </template>
    </v-data-table>
    <v-snackbar
      v-model="snackbar.status"
    >
      {{ snackbar.text }}

      <template v-slot:action="{ attrs }">
        <v-btn
          color="secondary"
          text
          v-bind="attrs"
          @click="snackbar.status = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import { monitorService } from "@/services/monitor.service";

export default {
  name: "Monitoring",
  data() {
    return {
      headers: [
        { text: 'Status', value: 'status' },
        { text: 'Created', value: 'created' },
        {
          text: '',
          value: 'actions',
          sortable: false
        }
      ],
      entries: [],
      snackbar: {
        status: false,
        text: ""
      },
      reason: "",
      dialog: false,
      editedIndex: -1,
      info: null,
      current_id: null
    }
  },
  mounted() {
    this.fetchEntries();
  },
  methods: {
    fetchEntries() {
      monitorService.get_entries().then(res => {
        this.entries = res.data;
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      })
    },
    getEntry(item) {
      monitorService.get_entry(item.id).then(res => {
        this.current_id = item.id;
        this.info = res.data;
        this.dialog = true;
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      })
    },
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.info = Object.assign({}, null)
        this.editedIndex = -1
      })
    },
    approve() {
      monitorService.approve_entry(this.current_id).then(res => {
        this.fetchEntries();
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      }).finally(() => {
        this.dialog = false;
      })
    },
    deny() {
      monitorService.deny_entry(this.current_id, this.reason).then(res => {
        this.fetchEntries();
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      }).finally(() => {
        this.dialog = false;
      })
    }
  }
}
</script>
